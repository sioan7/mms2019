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
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_ar.*


class ArActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var arrayView: Array<View>
    lateinit var arFragment: ArFragment

    lateinit var bearRenderable: ModelRenderable
    lateinit var catRenderable: ModelRenderable
    lateinit var cowRenderable: ModelRenderable
    lateinit var dogRenderable: ModelRenderable
    lateinit var elephantRenderable: ModelRenderable
    lateinit var ferretRenderable: ModelRenderable
    lateinit var hippopotamusRenderable: ModelRenderable
    lateinit var horseRenderable: ModelRenderable
    lateinit var koalaRenderable: ModelRenderable
    lateinit var lionRenderable: ModelRenderable
    lateinit var reindeerRenderable: ModelRenderable
    lateinit var wolverineRenderable: ModelRenderable

    private lateinit var mediaPlayer: MediaPlayer

    private var selected = 1

    override fun onClick(view: View?) {
        selected = when (view!!.id) {
            R.id.bear -> 1
            R.id.cat -> 2
            R.id.cow -> 3
            R.id.dog -> 4
            R.id.elephant -> 5
            R.id.ferret -> 6
            R.id.hippopotamus -> 7
            R.id.horse -> 8
            R.id.koala_bear -> 9
            R.id.lion -> 10
            R.id.reindeer -> 11
            R.id.wolverine -> 12
            else -> 1
        }
        mySetBackground(view.id)
    }

    private fun mySetBackground(id: Int) {
        for (i in arrayView.indices) {
            if (arrayView[i].id == id) {
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"))
            } else {
                arrayView[i].setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        setupArray()
        setupClickListener()
        setupModel()

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
        if (selected == 1) {
            val bear = TransformableNode(arFragment.transformationSystem)
            bear.setParent(anchorNode)

            val renderableCopy = bearRenderable.makeCopy()
            val changedMaterial = renderableCopy.material.makeCopy()
            changedMaterial.setFloat3("baseColorTint", com.google.ar.sceneform.rendering.Color(1f, 0f, 0f, 0.5f))

            renderableCopy.material = changedMaterial


            bear.renderable = renderableCopy
            bear.localScale = Vector3(50f, 50f, 50f)
            bear.scaleController.maxScale = 50f
            bear.scaleController.minScale = 10f
            bear.select()

            addName(anchorNode, bear, "Bear")
            //mediaPlayer.start()
        } else if (selected == 2) {
            val cat = TransformableNode(arFragment.transformationSystem)
            cat.setParent(anchorNode)
            cat.renderable = catRenderable
            cat.localScale = Vector3(50f, 50f, 50f)
            cat.scaleController.maxScale = 50f
            cat.scaleController.minScale = 10f
            cat.select()

            addName(anchorNode, cat, "Cat")
        } else if (selected == 3) {
            val cow = TransformableNode(arFragment.transformationSystem)
            cow.setParent(anchorNode)
            cow.renderable = cowRenderable
            cow.localScale = Vector3(50f, 50f, 50f)
            cow.scaleController.maxScale = 50f
            cow.scaleController.minScale = 10f
            cow.select()

            addName(anchorNode, cow, "Cow")

        } else if (selected == 4) {
            val dog = TransformableNode(arFragment.transformationSystem)
            dog.setParent(anchorNode)
            dog.renderable = dogRenderable
            dog.localScale = Vector3(50f, 50f, 50f)
            dog.scaleController.maxScale = 50f
            dog.scaleController.minScale = 10f
            dog.select()

            addName(anchorNode, dog, "Dog")
        } else if (selected == 5) {
            val elephant = TransformableNode(arFragment.transformationSystem)
            elephant.setParent(anchorNode)
            elephant.renderable = elephantRenderable
            elephant.localScale = Vector3(50f, 50f, 50f)
            elephant.scaleController.maxScale = 50f
            elephant.scaleController.minScale = 10f
            elephant.select()

            addName(anchorNode, elephant, "Elephant")
        } else if (selected == 6) {
            val ferret = TransformableNode(arFragment.transformationSystem)
            ferret.setParent(anchorNode)
            ferret.renderable = ferretRenderable
            ferret.localScale = Vector3(50f, 50f, 50f)
            ferret.scaleController.maxScale = 50f
            ferret.scaleController.minScale = 10f
            ferret.select()

            addName(anchorNode, ferret, "Ferret")
        } else if (selected == 7) {
            val hippopotamus = TransformableNode(arFragment.transformationSystem)
            hippopotamus.setParent(anchorNode)
            hippopotamus.renderable = hippopotamusRenderable
            hippopotamus.localScale = Vector3(50f, 50f, 50f)
            hippopotamus.scaleController.maxScale = 50f
            hippopotamus.scaleController.minScale = 10f
            hippopotamus.select()

            addName(anchorNode, hippopotamus, "Hippopotamus")
        } else if (selected == 8) {
            val horse = TransformableNode(arFragment.transformationSystem)
            horse.setParent(anchorNode)
            horse.renderable = horseRenderable
            horse.localScale = Vector3(50f, 50f, 50f)
            horse.scaleController.maxScale = 50f
            horse.scaleController.minScale = 10f
            horse.select()

            addName(anchorNode, horse, "Horse")
        } else if (selected == 9) {
            val koala = TransformableNode(arFragment.transformationSystem)
            koala.setParent(anchorNode)
            koala.renderable = koalaRenderable
            koala.localScale = Vector3(50f, 50f, 50f)
            koala.scaleController.maxScale = 50f
            koala.scaleController.minScale = 10f
            koala.select()

            addName(anchorNode, koala, "Koala")
        } else if (selected == 10) {
            val lion = TransformableNode(arFragment.transformationSystem)
            lion.setParent(anchorNode)
            lion.renderable = lionRenderable
            lion.localScale = Vector3(50f, 50f, 50f)
            lion.scaleController.maxScale = 50f
            lion.scaleController.minScale = 10f
            lion.select()

            addName(anchorNode, lion, "Lion")
        } else if (selected == 11) {
            val reindeer = TransformableNode(arFragment.transformationSystem)
            reindeer.setParent(anchorNode)
            reindeer.renderable = reindeerRenderable
            reindeer.localScale = Vector3(50f, 50f, 50f)
            reindeer.scaleController.maxScale = 50f
            reindeer.scaleController.minScale = 10f
            reindeer.select()

            addName(anchorNode, reindeer, "Reindeer")
        } else if (selected == 12) {
            val wolverine = TransformableNode(arFragment.transformationSystem)
            wolverine.setParent(anchorNode)
            wolverine.renderable = wolverineRenderable
            wolverine.localScale = Vector3(50f, 50f, 50f)
            wolverine.scaleController.maxScale = 50f
            wolverine.scaleController.minScale = 10f
            wolverine.select()

            addName(anchorNode, wolverine, "Wolverine")
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

    private fun setupModel() {
        ModelRenderable.builder()
                .setSource(this, R.raw.bear)
                .build()
                .thenAccept { modelRenderable -> bearRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the bearRenderable", Toast.LENGTH_LONG).show()
                    null
                }
        ModelRenderable.builder()
                .setSource(this, R.raw.cat)
                .build()
                .thenAccept { modelRenderable -> catRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the catRenderable", Toast.LENGTH_LONG).show()
                    null
                }
        ModelRenderable.builder()
                .setSource(this, R.raw.cow)
                .build()
                .thenAccept { modelRenderable -> cowRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the cowRenderable", Toast.LENGTH_LONG).show()
                    null
                }
        ModelRenderable.builder()
                .setSource(this, R.raw.dog)
                .build()
                .thenAccept { modelRenderable -> dogRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the dogRenderable", Toast.LENGTH_LONG).show()
                    null
                }
        ModelRenderable.builder()
                .setSource(this, R.raw.elephant)
                .build()
                .thenAccept { modelRenderable -> elephantRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the elephantRenderable", Toast.LENGTH_LONG).show()
                    null
                }
        ModelRenderable.builder()
                .setSource(this, R.raw.ferret)
                .build()
                .thenAccept { modelRenderable -> ferretRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the ferretRenderable", Toast.LENGTH_LONG).show()
                    null
                }
        ModelRenderable.builder()
                .setSource(this, R.raw.hippopotamus)
                .build()
                .thenAccept { modelRenderable -> hippopotamusRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the hippopotamusRenderable", Toast.LENGTH_LONG).show()
                    null
                }
        ModelRenderable.builder()
                .setSource(this, R.raw.horse)
                .build()
                .thenAccept { modelRenderable -> horseRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the horseRenderable", Toast.LENGTH_LONG).show()
                    null
                }

        ModelRenderable.builder()
                .setSource(this, R.raw.koala_bear)
                .build()
                .thenAccept { modelRenderable -> koalaRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the koalaRenderable", Toast.LENGTH_LONG).show()
                    null
                }
        ModelRenderable.builder()
                .setSource(this, R.raw.lion)
                .build()
                .thenAccept { modelRenderable -> lionRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the lionRenderable", Toast.LENGTH_LONG).show()
                    null
                }
        ModelRenderable.builder()
                .setSource(this, R.raw.reindeer)
                .build()
                .thenAccept { modelRenderable -> reindeerRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the reindeerRenderable", Toast.LENGTH_LONG).show()
                    null
                }
        ModelRenderable.builder()
                .setSource(this, R.raw.wolverine)
                .build()
                .thenAccept { modelRenderable -> wolverineRenderable = modelRenderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@ArActivity, "Unable to load the wolverineRenderable", Toast.LENGTH_LONG).show()
                    null
                }

    }

    private fun setupClickListener() {
        for (i in arrayView.indices) {
            arrayView[i].setOnClickListener(this)

        }
    }

    private fun setupArray() {
        arrayView = arrayOf(
                bear,
                cat,
                cow,
                dog,
                elephant,
                ferret,
                hippopotamus,
                horse,
                koala_bear,
                lion,
                reindeer,
                wolverine
        )
    }
}
