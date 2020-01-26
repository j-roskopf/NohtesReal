package xute.markdeditor.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import xute.markdeditor.R;
import xute.markdeditor.models.ComponentTag;
import xute.markdeditor.utilities.ImageHelper;

public class ImageComponentItem extends FrameLayout {
  ImageView imageView;
  ProgressBar imageUploadProgressBar;
  ImageView retryUpload;
  TextView statusMessage;
  ImageView removeImageButton;

  private String filePath;
  private Context mContext;
  private ImageComponentListener imageComponentListener;

  public ImageComponentItem(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.image_component_item, this);
    imageView = view.findViewById(R.id.image);
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
        setImageInformation(filePath);
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
  }

  public void setImageInformation(String filePath) {
    this.filePath = filePath;
    loadImage(filePath);
  }

  private int getSelfIndex() {
    ComponentTag tag = (ComponentTag) getTag();
    return tag.getComponentIndex();
  }

  private void loadImage(String filePath) {
    ImageHelper.load(mContext, imageView, filePath);
  }

  public ImageComponentItem(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFocus() {
    imageView.setEnabled(true);
  }

  public void setImageComponentListener(ImageComponentListener imageComponentListener) {
    this.imageComponentListener = imageComponentListener;
  }

  public interface ImageComponentListener {
    void onImageRemove(int removeIndex);
  }
}
