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
    val modelName: String
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
            Triple(R.raw.bear, bear, "Bear"),
            Triple(R.raw.cat, cat, "Cat"),
            Triple(R.raw.cow, cow, "Cow"),
            Triple(R.raw.dog, dog, "Dog"),
            Triple(R.raw.elephant, elephant, "Elephant"),
            Triple(R.raw.ferret, ferret, "Ferret"),
            Triple(R.raw.hippopotamus, hippopotamus, "Hippopotamus"),
            Triple(R.raw.horse, horse, "Horse"),
            Triple(R.raw.koala_bear, koala_bear, "Koala Bear"),
            Triple(R.raw.lion, lion, "Lion"),
            Triple(R.raw.reindeer, reindeer, "Reindeer"),
            Triple(R.raw.wolverine, wolverine, "Wolverine")
        ).forEach { triple ->
            ModelRenderable
                .builder()
                .setSource(this, triple.first).build()
                .thenAccept {
                    val arObject = ARObject(triple.first, it, triple.second, triple.third)
                    arObject.view.setOnClickListener(this@ArActivity)
                    arData[triple.first] = arObject
                }
                .exceptionally {
                    Toast.makeText(this@ArActivity, "Unable to load model ${triple.first}", Toast.LENGTH_LONG).show()
                    null
                }
        }
    }
}
