package test.linkaja.testapp.util

import android.content.Context
import android.util.Patterns

import android.text.TextUtils
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.File
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


class Utility {
    companion object{
        /**
         * email validation checker
         *
         * @param target
         * @return
         */
        fun isValidEmail(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }


        /**
         * reformat date string
         *
         * @param dateString old value that will be converted
         * @param oldFormat old format before conversion
         * @param newFormat new format determined
         * @return formatted string with new format
         */
        fun reformatDate(dateString :String, oldFormat: String, newFormat: String) : String{
            val id = Locale("in", "ID")
            val oldSdf = SimpleDateFormat(oldFormat, id)
            val newSdf = SimpleDateFormat(newFormat, id)

            val date = oldSdf.parse(dateString)
            return newSdf.format(date)
        }

        fun loadJson(context: Context, fileName:String) : String{
            var json: String? = null
            json = try {
                //JSON file is in src/main/assets/dummData.json
                val inputStream: InputStream = context.assets.open(fileName)
                val size: Int = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                String(buffer, Charsets.UTF_8)

            } catch (ex: IOException) {
                return ""
            }
            return json!!
        }

        /**
         * to rotate image on upload process
         *
         * similar issue on [upload image](https://stackoverflow.com/questions/39515637/camera-image-gets-rotated-when-i-upload-to-server)
         * @param imageFile image saved path
         * @return byte array
         */
        fun getStreamByteFromImage(imageFile: File): ByteArray {
            var photoBitmap = BitmapFactory.decodeFile(imageFile.path)
            val stream = ByteArrayOutputStream()
            val imageRotation: Int = getImageRotation(imageFile)
            if (imageRotation != 0) photoBitmap =
                getBitmapRotatedByDegree(photoBitmap, imageRotation)
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            return stream.toByteArray()
        }

        /**
         * get image rotation
         *
         * similar issue on [rotate camera upload](https://stackoverflow.com/questions/39515637/camera-image-gets-rotated-when-i-upload-to-server)
         *
         * @param imageFile image saved path
         * @return int value
         */
        fun getImageRotation(imageFile: File): Int {
            var exif: ExifInterface? = null
            var exifRotation = 0
            try {
                exif = ExifInterface(imageFile.path)
                exifRotation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return if (exif == null) 0 else exifToDegrees(exifRotation)
        }

        /**
         * convert exif to degree
         *
         * for more about [exif](https://developer.android.com/reference/android/media/ExifInterface)
         *
         * @param rotation it may 0, 90, 180 or 270 degree
         * @return
         */
        private fun exifToDegrees(rotation: Int): Int {
            return when (rotation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        }

        private fun getBitmapRotatedByDegree(bitmap: Bitmap, rotationDegree: Int): Bitmap? {
            val matrix = Matrix()
            matrix.preRotate(rotationDegree.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }
}