package com.sample.managingfolder

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.sample.managingfolder.databinding.ActivityMainBinding
import com.sample.managingfolder.databinding.AlertdialogEdittextBinding
import java.io.File




class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //앱 전용 디렉토리 토스크로 출력
        binding.btnGetDir.setOnClickListener{
           toast(filesDir.toString())
        }

        //디렉토리 생성
        binding.btnMkdir.setOnClickListener {
            val alertDialogEditText = AlertdialogEdittextBinding.inflate(layoutInflater)
            val builder = AlertDialog.Builder(this)
                .setTitle("Input Directory Name")
                .setView(alertDialogEditText.root)
                .setPositiveButton("OK"){ dialogInterface: DialogInterface, i: Int ->
                    makeDirectory(alertDialogEditText.editText.text.toString())
                }
                .show()
        }

        //파일 리스트 보기
        binding.btnFileList.setOnClickListener {
            binding.textView.text = null
            val path = File(filesDir.toString())
            val files = path.listFiles()
            var strFileList: String? = "App Directory File Lists\n"
            for(file in files){
                strFileList += "->" + file.name + "\n"
            }
            binding.textView.text = strFileList
        }

        // 디렉토리 삭제
       binding.btnDeleteDir.setOnClickListener {
            val files = File(filesDir.toString()).listFiles()
            val selectedItemIndex = ArrayList<Int>()
            var filesName = Array(files.size) { item -> "" }
            for(i in files.indices){
                filesName[i] = files[i].name
            }
            val builder = AlertDialog.Builder(this)
                .setTitle("삭제할 디렉토리 선택")
                .setMultiChoiceItems(filesName, null){ dialogInterface: DialogInterface, i: Int, b: Boolean ->
                    if(b){
                        selectedItemIndex.add(i)
                    }else if(selectedItemIndex.contains(i)){
                        selectedItemIndex.remove(i)
                    }
                }
                .setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                    for(i in selectedItemIndex.indices){
                        val file = File("$filesDir/${filesName[selectedItemIndex[i]]}")
                        file.delete()
                    }
                }
            val dialog = builder.create()
            dialog.show()
        }


    }


    fun toast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }


    fun makeDirectory(folderName:String){
        val path = File("$filesDir/$folderName")
        if(!path.exists()){
            path.mkdirs()
        }else{
            toast("$folderName is exists")
        }
    }



}












