package com.guguma.guguma_application

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CreateUserActivity : AppCompatActivity() {

    private lateinit var checkCamera: CheckBox
    private lateinit var checkGallery: CheckBox
    private lateinit var ButtonStartUserNickname: Button

    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private val GALLERY_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else Manifest.permission.READ_EXTERNAL_STORAGE

    private val PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user_start)

        checkCamera = findViewById(R.id.checkCamera)
        checkGallery = findViewById(R.id.checkGallery)
        ButtonStartUserNickname = findViewById(R.id.ButtonStartUserNickname)

        checkCamera.setOnClickListener { checkAndRequestPermission(CAMERA_PERMISSION, checkCamera) }
        checkGallery.setOnClickListener { checkAndRequestPermission(GALLERY_PERMISSION, checkGallery) }

        // ���� ��ư ���� Ȯ��
        updateStartButtonState()
    }

    // ���� ��û �� üũ�ڽ� ���� ������Ʈ
    private fun checkAndRequestPermission(permission: String, checkBox: CheckBox) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "������ �̹� ���Ǿ����ϴ�.", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
        }
        updateStartButtonState()
    }

    // ���� ��û ��� ó��
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            permissions.forEachIndexed { index, permission ->
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    when (permission) {
                        CAMERA_PERMISSION -> checkCamera.isChecked = true
                        GALLERY_PERMISSION -> checkGallery.isChecked = true
                    }
                } else {
                    Toast.makeText(this, "������ �ʿ��մϴ�.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        updateStartButtonState()
    }

    // ���� ��ư Ȱ��ȭ ���� ������Ʈ
    private fun updateStartButtonState() {
        ButtonStartUserNickname.isEnabled = checkCamera.isChecked && checkGallery.isChecked

        ButtonStartUserNickname.setOnClickListener {
            if (ButtonStartUserNickname.isEnabled) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}