package xute.markdeditor.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import xute.markdeditor.R;
import xute.markdeditor.models.ComponentTag;
import xute.markdeditor.models.ImageComponentModel;
import xute.markdeditor.utilities.ImageHelper;

public class ImageComponentItem extends FrameLayout {
  ImageView imageView;
  EditText captionEt;
  ProgressBar imageUploadProgressBar;
  ImageView retryUpload;
  TextView statusMessage;
  ImageView removeImageButton;

  private String downloadUrl;
  private String caption;
  private String filePath;
  private Context mContext;
  private ImageComponentListener imageComponentListener;

  public ImageComponentItem(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    downloadUrl = null;
    View view = LayoutInflater.from(context).inflate(R.layout.image_component_item, this);
    imageView = view.findViewById(R.id.image);
    captionEt = view.findViewById(R.id.caption);
    retryUpload = view.findViewById(R.id.retry_image_upload_btn);
    imageUploadProgressBar = view.findViewById(R.id.image_uploading_progress_bar);
    statusMessage = view.findViewById(R.id.message);
    removeImageButton = view.findViewById(R.id.removeImageBtn);
    attachListeners();
  }

  private void attachListeners() {
    retryUpload.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        setImageInformation(filePath,"");
      }
    });

    removeImageButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (imageComponentListener != null) {
          imageComponentListener.onImageRemove(getSelfIndex());
        }
      }
    });

    captionEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        checkReturnPressToInsertNewTextComponent(charSequence);
        setCaption(charSequence.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
  }

  public void setImageInformation(String filePath, String caption) {
    this.filePath = filePath;
    setCaption(caption);
    if (caption != null) {
      captionEt.setText(caption);
    }
    loadImage(filePath);
  }

  private int getSelfIndex() {
    ComponentTag tag = (ComponentTag) getTag();
    return tag.getComponentIndex();
  }

  private void checkReturnPressToInsertNewTextComponent(CharSequence charSequence) {
    int clen = charSequence.length();
    if (clen > 0) {
      String sequenceToCheckNewLineCharacter = (clen > 1) ? charSequence.subSequence(clen - 2, clen).toString()
       :
       charSequence.subSequence(clen - 1, clen).toString();
      boolean noReadableCharactersAfterCursor = sequenceToCheckNewLineCharacter.trim().length() == 0;
      //if last characters are [AB\n<space>] or [AB\n] then we insert new TextComponent
      //else if last characters are [AB\nC] ignore the insert.
      if (sequenceToCheckNewLineCharacter.contains("\n") && noReadableCharactersAfterCursor) {
        if (imageComponentListener != null) {
          imageComponentListener.onExitFromCaptionAndInsertNewTextComponent(getSelfIndex() + 1);
        }
      }
    }
  }

  private void loadImage(String filePath) {
    ImageHelper.load(mContext, imageView, filePath);
  }


  private void hideExtraInfroWithDelay() {
    new android.os.Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        statusMessage.setVisibility(GONE);
        removeImageButton.setVisibility(GONE);
        retryUpload.setVisibility(GONE);
        imageUploadProgressBar.setVisibility(GONE);
      }
    }, 2000);
  }

  public ImageComponentItem(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
    ComponentTag tag = (ComponentTag) getTag();
    ((ImageComponentModel) tag.getComponent()).setUrl(downloadUrl);
  }

  public void setFocus() {
    imageView.setEnabled(true);
    captionEt.requestFocus();
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
    ComponentTag tag = (ComponentTag) getTag();
    ((ImageComponentModel) tag.getComponent()).setCaption(caption);
  }


  public void setImageComponentListener(ImageComponentListener imageComponentListener) {
    this.imageComponentListener = imageComponentListener;
  }

  public interface ImageComponentListener {
    void onImageRemove(int removeIndex);

    void onExitFromCaptionAndInsertNewTextComponent(int currentIndex);
  }
}
