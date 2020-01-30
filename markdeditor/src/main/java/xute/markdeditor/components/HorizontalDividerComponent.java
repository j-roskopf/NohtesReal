package xute.markdeditor.components;

import android.content.Context;

//todo joe convert everything to kotlin
//todo joe add support for theming via library

public class HorizontalDividerComponent {
  Context context;

  public HorizontalDividerComponent(Context context) {
    this.context = context;
  }

  public HorizontalDividerComponentItem getNewHorizontalComponentItem() {
    return new HorizontalDividerComponentItem(context);
  }
}
