package javis.wearsyncservice;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class MirroredTextView extends TextView {

    public MirroredTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(0, getHeight());
        canvas.scale(1, -1);
        super.onDraw(canvas);
    }

}