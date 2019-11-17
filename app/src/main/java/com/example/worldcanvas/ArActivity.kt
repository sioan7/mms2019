package com.example.worldcanvas

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_ar.*

data class ARObject(
    val resourceId: Int,
    val model: ModelRenderable,
    val view: View,
    val modelName: String,
    val sound : MediaPlayer
)

class ArActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var arFragment: ArFragment

    private var arData = mutableMapOf<Int, ARObject>()
    private var selected: Int = 0

    override fun onClick(view: View?) {
        selected = arData.values.find { it.view.id == view!!.id }?.resourceId ?: 0
        mySetBackground(view!!.id)
    }

    private fun mySetBackground(id: Int) {
        arData.values.forEach { value ->
            value.view.setBackgroundColor(if (id == value.view.id) Color.parseColor("#80333639") else Color.TRANSPARENT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        setupARData()
        //selected = intent.getIntExtra("Position",0)
        mediaPlayer = MediaPlayer.create(this, R.raw.grizzlybearroar)

        arFragment = supportFragmentManager.findFragmentById(R.id.scene_form_fragment) as ArFragment

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            createModel(anchorNode, selected)
        }

    }

    private fun createModel(anchorNode: AnchorNode, selected: Int) {
        val node = TransformableNode(arFragment.transformationSystem)
        node.setParent(anchorNode)
        val arObject = arData[selected]
        if (arObject != null) {
            arObject.sound.start()
        }

//        Texture.builder()
//            .setSource(intent.getParcelableExtra("BITMAP") as Bitmap)
//            .setSource(Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888))
//            .build()

        val nodeSetup = {
            node.localScale = Vector3(50f, 50f, 50f)
            node.scaleController.maxScale = 50f
            node.scaleController.minScale = 10f
            node.select()
            addName(anchorNode, node, arObject?.modelName.orEmpty())
        }
        if (intent.hasExtra("COLOR")) {
            val customColor = intent.getIntExtra("COLOR", Color.MAGENTA)
            MaterialFactory
                .makeOpaqueWithColor(this, com.google.ar.sceneform.rendering.Color(customColor))
                .thenAccept {
                    val model = arObject?.model
                    model?.material = it
                    node.renderable = model
                    nodeSetup()
                }
        } else {
            node.renderable = arObject?.model
            nodeSetup()
        }
    }

    private fun addName(anchorNode: AnchorNode, node: TransformableNode, name: String) {
        ViewRenderable.builder().setView(this, R.layout.name_layout)
                .build()
                .thenAccept { viewRenderable ->
                    val nameView = TransformableNode(arFragment.transformationSystem)
                    nameView.localPosition = Vector3(0f, node.localPosition.y + 0.5f, 0f)
                    nameView.setParent(anchorNode)
                    nameView.renderable = viewRenderable
                    nameView.select()

                    val txt_name = viewRenderable.view as TextView
                    txt_name.text = name
                    txt_name.setOnClickListener {
                        anchorNode.setParent(null)
                    }

                }
    }

    private fun setupARData() {
        listOf(
            Quadruple(R.raw.bear, bear, "Bear",MediaPlayer.create(this, R.raw.bear_sound)),
                Quadruple(R.raw.cat, cat, "Cat",MediaPlayer.create(this, R.raw.cat_sound)),
                Quadruple(R.raw.cow, cow, "Cow",MediaPlayer.create(this, R.raw.cow_sound)),
                Quadruple(R.raw.dog, dog, "Dog",MediaPlayer.create(this, R.raw.dog_sound)),
                Quadruple(R.raw.elephant, elephant, "Elephant",MediaPlayer.create(this, R.raw.elephant_sound)),
                Quadruple(R.raw.ferret, ferret, "Ferret",MediaPlayer.create(this, R.raw.ferret_sound)),
                Quadruple(R.raw.hippopotamus, hippopotamus, "Hippopotamus",MediaPlayer.create(this, R.raw.hippopotamus_sound)),
                Quadruple(R.raw.horse, horse, "Horse",MediaPlayer.create(this, R.raw.horse_sound)),
                Quadruple(R.raw.koala_bear, koala_bear, "Koala Bear",MediaPlayer.create(this, R.raw.koala_sound)),
                Quadruple(R.raw.lion, lion, "Lion",MediaPlayer.create(this, R.raw.lion_sound)),
                Quadruple(R.raw.reindeer, reindeer, "Reindeer",MediaPlayer.create(this, R.raw.deer_sound)),
                Quadruple(R.raw.wolverine, wolverine, "Wolverine",MediaPlayer.create(this, R.raw.wolverine_sound))
        ).forEach { quadruple ->
            ModelRenderable
                .builder()
                .setSource(this, quadruple.first).build()
                .thenAccept {
                    val arObject = ARObject(quadruple.first, it, quadruple.second, quadruple.third,quadruple.fourth)
                    arObject.view.setOnClickListener(this@ArActivity)
                    arData[quadruple.first] = arObject
                }
                .exceptionally {
                    Toast.makeText(this@ArActivity, "Unable to load model ${quadruple.first}", Toast.LENGTH_LONG).show()
                    null
                }
        }
    }
}
