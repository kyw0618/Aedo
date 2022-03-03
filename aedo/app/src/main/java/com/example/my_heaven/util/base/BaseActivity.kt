package com.example.my_heaven.util.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.databinding.ToastCustomBinding
import com.example.my_heaven.util.`object`.ActivityControlManager
import com.example.my_heaven.util.`object`.Constant
import com.example.my_heaven.util.alert.AlertDialogManager
import com.example.my_heaven.util.alert.LoadingDialog
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.common.CommonData
import com.example.my_heaven.util.log.LLog.e
import com.example.my_heaven.view.login.LoginActivity
import com.example.my_heaven.view.main.MainActivity
import com.example.my_heaven.view.main.SideMenuActivity
import com.example.my_heaven.view.main.detail.center.CenterActivity
import com.example.my_heaven.view.main.detail.make.MakeActivity
import com.example.my_heaven.view.main.detail.modify.ModifyActivity
import com.example.my_heaven.view.main.detail.search.SearchActivity
import com.example.my_heaven.view.main.detail.send.SendActivity
import com.example.my_heaven.view.main.detail.shop.ShopActivity
import com.example.my_heaven.view.side.list.ListActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.one_button_dialog.view.*
import kotlinx.android.synthetic.main.two_button_dialog.view.*
import org.json.JSONException
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

open class BaseActivity : AppCompatActivity() {
    internal open var instance: BaseActivity?=null
    var ResultView: ActivityResultLauncher<Intent>? = null
    var comm: CommonData? = CommonData().getInstance()
    var alert: AlertDialogManager? = null

    internal var dialog : Dialog ?= null

    internal val realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        instance = this
        alert = AlertDialogManager(this)
        dialog = LoadingDialog(this)
        val window = window
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    internal fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.progress)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onResume() {
        super.onResume()
        if (!isNetworkAvailable) {
            networkDialog()
            return
        }
    }

    open fun onTitleLeftClick(v: View?) {}

    open fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    open fun moveAndFinishActivity(activity: Class<*>?) {
        ActivityControlManager.moveAndFinishActivity(this, activity)
    }

    open fun hideSoftKeyboard(v: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    open fun hideSoftKeyboard() {
        val v = currentFocus
        if (v != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw)
                actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(
                    NetworkCapabilities.TRANSPORT_BLUETOOTH
                ))
            } else {
                val nwInfo = connectivityManager.activeNetworkInfo
                nwInfo != null && nwInfo.isConnected
            }
        }

    val hash: String?
        get() {
            val context = applicationContext
            val pm = context.packageManager
            val packageName = context.packageName
            var cert: String? = null
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    @SuppressLint("WrongConstant") val packageInfo = packageManager.getPackageInfo(
                        getPackageName(),
                        PackageManager.GET_SIGNING_CERTIFICATES
                    )
                    val signatures = packageInfo.signingInfo.apkContentsSigners
                    val md = MessageDigest.getInstance("SHA1")
                    for (signature in signatures) {
                        md.update(signature.toByteArray())
                        cert = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                        cert = cert.replace(
                            Objects.requireNonNull(System.getProperty("line.separator")).toRegex(),
                            ""
                        )
                        Log.i("test", "Cert=$cert")
                    }
                } else {
                    @SuppressLint("PackageManagerGetSignatures") val packageInfo =
                        pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                    val certSignature = packageInfo.signatures[0]
                    val msgDigest = MessageDigest.getInstance("SHA1")
                    msgDigest.update(certSignature.toByteArray())
                    cert = Base64.encodeToString(msgDigest.digest(), Base64.DEFAULT)
                    cert = cert.replace(
                        Objects.requireNonNull(System.getProperty("line.separator")).toRegex(), ""
                    )
                    Log.i("test", "Cert=$cert")
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e("예외발생")
            } catch (e: NoSuchAlgorithmException) {
                e("예외발생")
            }
            return cert
        }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }

    @SuppressLint("ResourceType")
    internal fun serverDialog() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.server_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }

    }

    internal fun networkDialog() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.network_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
    }

    internal fun rootingDialog() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.warning_rooting)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
    }

    internal fun UpdateDialog() {
        val myLayout = layoutInflater.inflate(R.layout.two_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.update_check)
        val dialog = build.create()
        dialog.show()

        myLayout.finish_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
        myLayout.update_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
    }

    internal fun moveSide() {
        val intent = Intent(this, SideMenuActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveCenter() {
        val intent = Intent(this, CenterActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveMake() {
        val intent = Intent(this, MakeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        dialog?.dismiss()
        finish()
    }

    internal fun moveSend() {
        val intent = Intent(this, SendActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        dialog?.dismiss()
        finish()
    }

    internal fun moveRE() {
        val intent = Intent(this, ModifyActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        dialog?.dismiss()
        finish()
    }

    internal fun moveSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        dialog?.dismiss()
        finish()
    }

    internal fun moveShop() {
        val intent = Intent(this, ShopActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        dialog?.dismiss()
        finish()
    }

    internal fun moveList() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        dialog?.dismiss()
        finish()
    }

    internal fun moveLogins() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        dialog?.dismiss()
        finish()
    }


    open fun removeGps() {}
}