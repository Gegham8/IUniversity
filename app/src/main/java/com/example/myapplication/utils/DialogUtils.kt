import android.app.AlertDialog
import android.content.Context
import com.example.myapplication.enums.DialogMessage
import com.example.myapplication.enums.Status

object DialogUtils {
    fun showErrorDialog(context: Context, message: DialogMessage) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage(message.message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun showDialog(context: Context, title: Status, message : DialogMessage) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title.value)
        builder.setMessage(message.message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
