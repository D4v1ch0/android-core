package rp3.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by magno_000 on 27/07/2015.
 */
public class DrawableUtils {

    public static Drawable scaleImage (Drawable image, float scaleFactor, Context ctx) {

        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }

        Bitmap b = ((BitmapDrawable)image).getBitmap();

        int sizeX = Math.round(image.getIntrinsicWidth() * scaleFactor);
        int sizeY = Math.round(image.getIntrinsicHeight() * scaleFactor);

        try
        {
            Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);
            image = new BitmapDrawable(ctx.getResources(), bitmapResized);
        }
        catch (OutOfMemoryError oem)
        {
            image = scaleImage(image, scaleFactor * 0.9f, ctx);
        }

        return image;

    }
}
