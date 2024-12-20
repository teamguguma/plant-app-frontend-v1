package com.guguma.guguma_application

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.guguma.guguma_application.dto.PlantDto
import androidx.recyclerview.widget.RecyclerView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.Response
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class PlantAdapter(

    private val context: Context,
    private val plantList: MutableList<PlantDto>,
    private val onDelete: (Long) -> Unit, // 삭제 버튼 클릭 시 호출할 콜백
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {
    

    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantNameTextView: TextView = itemView.findViewById(R.id.plantNameTextView)
        val plantNicknameTextView: TextView = itemView.findViewById(R.id.plantNicknameTextView)
        val plantImageView: ImageView = itemView.findViewById(R.id.plantImageView)

        fun bind(plant: PlantDto) {
            plantNameTextView.text = plant.name
            //val contentDescription = "${plant.name}, 촬영 날짜: ${plant.date}"
            //plantNicknameTextView.text = plant.nickname
            Glide.with(context).load(plant.imageUrl).into(plantImageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_plant, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plantList[position]
        holder.bind(plant)


        // 클릭 이벤트: 상세 화면 이동 또는 체크박스 상태 변경
        holder.itemView.setOnClickListener {
                val intent = Intent(context, AddPlantActivity::class.java).apply {
                    putExtra("plantName", plant.name)
                    putExtra("plantImageUrl", plant.imageUrl)
                }
                context.startActivity(intent)
        }

    }

    // getItemCount: 아이템 총 개수 반환 (최대 10개만)
    override fun getItemCount(): Int {
        return if (plantList.size > 10) 10 else plantList.size // 최대 10개만 반환
    }

    // 특정 위치의 데이터를 가져오는 getItem 메서드
    fun getItem(position: Int): PlantDto {
        return plantList[position]
    }

    // 데이터 업데이트 메서드
    fun updateData(newPlantList: List<PlantDto>) {
        plantList.clear()
        plantList.addAll(newPlantList)
        notifyDataSetChanged() // 데이터 변경 후 UI 갱신
    }


    // 아이템 추가 메서드 (Create)
    fun addItem(plant: PlantDto) {
        plantList.add(plant)
        notifyItemInserted(plantList.size - 1)
    }

    // 아이템 수정 메서드 (Update)
    fun updateItem(position: Int, updatedPlant: PlantDto) {
        plantList[position] = updatedPlant
        notifyItemChanged(position)
    }
}