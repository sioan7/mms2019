package com.example.worldcanvas

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_ar.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

data class ARObject(
        val resourceId: Int,
        val model: ModelRenderable,
        val view: View,
        val modelName: String,
        val sound: MediaPlayer
)

class ArActivity : AppCompatActivity(), View.OnClickListener, Scene.OnUpdateListener {


    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var arFragment: ArFragment
    private var btnSS: Button? = null
    private var btnshare: Button? = null
    private var sharePath = "no"
    private var createdInitialObject = false

    private var arData = mutableMapOf<Int, ARObject>()
    private var selected: Int = 0
    private var checker: Int = 0
    private lateinit var context:Context
    override fun onClick(view: View?) {
        selected = view!!.tag.toString().toInt()
        mySetBackground(view.id)
    }

    override fun onBackPressed() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
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
        context = applicationContext
        //selected = intent.getIntExtra("Position",0)
        mediaPlayer = MediaPlayer.create(this, R.raw.grizzlybearroar)
        selected = intent.getIntExtra("Position", 0)
        arFragment = supportFragmentManager.findFragmentById(R.id.scene_form_fragment) as ArFragment

        arFragment.arSceneView.scene.addOnUpdateListener(this@ArActivity)

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            createModel(anchorNode)
        }

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        requestReadPermissions()

        btnSS = findViewById(R.id.btnSS)
        btnshare = findViewById(R.id.btnShare)


        btnSS!!.setOnClickListener {
            takePhoto(arFragment)
            Toast.makeText(applicationContext, "Screenshot was taken and saved in your gallery", Toast.LENGTH_LONG)
                    .show()
            Thread.sleep(500)
        }


        btnshare!!.setOnClickListener {
                share(sharePath)

        }


    }

    fun takePhoto(arFragment: ArFragment) {
        //final String filename = generateFilename();
        /*ArSceneView view = fragment.getArSceneView();*/
        val mSurfaceView = arFragment.arSceneView
        // Create a bitmap the size of the scene view.
        val bitmap = Bitmap.createBitmap(mSurfaceView.width, mSurfaceView.height,
                Bitmap.Config.ARGB_8888)

        // Create a handler thread to offload the processing of the image.
        val handlerThread = HandlerThread("PixelCopier")
        handlerThread.start()

        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

        PixelCopy.request(mSurfaceView, bitmap, { copyResult ->
            run {
                if (copyResult == PixelCopy.SUCCESS) {
                    try {
                        // image naming and path  to include sd card  appending name you choose for file
                        val mPath = context.getExternalFilesDir(null)!!.absolutePath + ".jpeg"


                        val imageFile = File(mPath)

                        val outputStream = FileOutputStream(imageFile)
                        val quality = 100
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                        outputStream.flush()
                        outputStream.close()

                        //setting screenshot in imageview
                        val filePath = imageFile.path

                        sharePath = filePath


                        MediaStore.Images.Media.insertImage(contentResolver, filePath, imageFile.name, imageFile.name)

                    } catch (e: Throwable) {
                        // Several error may come out with file handling or DOM
                        e.printStackTrace()
                    }
                }
                handlerThread.quitSafely()



            }
        }, Handler(handlerThread.looper))

    }


    private fun share(sharePath: String) {

        Log.d("ffff", sharePath)
        val file = File(sharePath)
        val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(intent)

    }

    private fun requestReadPermissions() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(applicationContext, "All permissions are granted by user!", Toast.LENGTH_SHORT)
                                    .show()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            Toast.makeText(applicationContext, "You will not be able to take screenshot and share it with your friends", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).withErrorListener(object : PermissionRequestErrorListener {
                    override fun onError(error: DexterError) {
                        Toast.makeText(applicationContext, "Some Error! ", Toast.LENGTH_SHORT).show()
                    }
                })
                .onSameThread()
                .check()
    }

    override fun onUpdate(p0: FrameTime?) {
//        //get the frame from the scene for shorthand
//        val frame = arFragment.arSceneView.arFrame
//        //val arObject = arData[selected]
//
//        if (frame != null && checker == 0) {
//            //get the trackables to ensure planes are detected
//            val var3 = frame.getUpdatedTrackables(Plane::class.java).iterator()
//            while (var3.hasNext()) {
//                val plane = var3.next() as Plane
//
//                //If a plane has been detected & is being tracked by ARCore
//                if (plane.trackingState == TrackingState.TRACKING) {
//
//                    //Hide the plane discovery helper animation
//                    arFragment.planeDiscoveryController.hide()
//
//
//                    //Get all added anchors to the frame
//                    val iterableAnchor = frame.updatedAnchors.iterator()
//
//                    //place the first object only if no previous anchors were added
//                    if (!iterableAnchor.hasNext()) {
//                        //Perform a hit test at the center of the screen to place an object without tapping
//                        val hitTest = frame.hitTest(screenCenter().x, screenCenter().y)
//
//                        //iterate through all hits
//                        val hitTestIterator = hitTest.iterator()
//                        while (hitTestIterator.hasNext()) {
//                            if (!createdInitialObject) {
//                                val hitResult = hitTestIterator.next()
//
//                                //Create an anchor at the plane hit
//                                val modelAnchor = plane.createAnchor(hitResult.hitPose)
//
//                                //Attach a node to this anchor with the scene as the parent
//                                val anchorNode = AnchorNode(modelAnchor)
//                                anchorNode.setParent(arFragment.arSceneView.scene)
//
//
//                                createModel(anchorNode)
//                                //Alter the real world position to ensure object renders on the table top. Not somewhere inside.
//                                checker = 1
//                                createdInitialObject = true
//                            }
//                        }
//                    }
//                }
//            }
//        }

    }

    //A method to find the screen center. This is used while placing objects in the scene
    private fun screenCenter(): Vector3 {
        val vw = findViewById<View>(android.R.id.content)
        return Vector3(vw.width / 2f, vw.height / 2f, 0f)
    }

    private fun createModel(anchorNode: AnchorNode) {
        val node = TransformableNode(arFragment.transformationSystem)
        node.setParent(anchorNode)
        val arObject = arData[selected]
        arObject?.sound?.start()

        val nodeSetup = {
            node.localScale = Vector3(50f, 50f, 50f)
            node.scaleController.maxScale = 50f
            node.scaleController.minScale = 10f
            node.select()
            addName(anchorNode, node, arObject?.modelName.orEmpty())
            node.worldPosition = Vector3(
                anchorNode.anchor?.pose!!.tx(),
                anchorNode.anchor?.pose!!.compose(Pose.makeTranslation(0f, 0.05f, 0f)).ty(),
                anchorNode.anchor?.pose!!.tz()
            )
        }
        if (intent.hasExtra("PATTERN1") || intent.hasExtra("PATTERN2") || intent.hasExtra("ANIMAL_IMAGE")) {
            val bmp = when {
                intent.hasExtra("PATTERN1") -> createPattern(intent.getIntegerArrayListExtra("COLORS") ?: listOf(), 1)
                intent.hasExtra("PATTERN2") -> createPattern(intent.getIntegerArrayListExtra("COLORS") ?: listOf(), 2)
                else -> {
                    val byteArray = intent.getByteArrayExtra("ANIMAL_IMAGE")
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                }
            }
            Texture
                .builder()
                .setSource(bmp)
                .build()
                .thenAccept { texture ->
                    MaterialFactory
                        .makeOpaqueWithTexture(this, texture)
                        .thenAccept {
                            val model = arData[selected]?.model
                            model?.material = it
                            node.renderable = model
                            arData[selected]?.sound?.start()
                            nodeSetup()
                        }
                }
        } else {
            node.renderable = arData[selected]?.model
            arData[selected]?.sound?.start()
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

                    val txtName = viewRenderable.view as TextView
                    txtName.text = name
                    txtName.setOnClickListener {
                        anchorNode.setParent(null)
                    }

                }
    }

    private fun setupARData() {
        listOf(
                Quadruple(R.raw.bear, bear, "Bear", MediaPlayer.create(this, R.raw.bear_sound)),
                Quadruple(R.raw.cat, cat, "Cat", MediaPlayer.create(this, R.raw.cat_sound)),
                Quadruple(R.raw.cow, cow, "Cow", MediaPlayer.create(this, R.raw.cow_sound)),
                Quadruple(R.raw.dog, dog, "Dog", MediaPlayer.create(this, R.raw.dog_sound)),
                Quadruple(R.raw.elephant, elephant, "Elephant", MediaPlayer.create(this, R.raw.elephant_sound)),
                Quadruple(R.raw.ferret, ferret, "Ferret", MediaPlayer.create(this, R.raw.ferret_sound)),
                Quadruple(R.raw.hippopotamus, hippopotamus, "Hippopotamus", MediaPlayer.create(this, R.raw.hippopotamus_sound)),
                Quadruple(R.raw.horse, horse, "Horse", MediaPlayer.create(this, R.raw.horse_sound)),
                Quadruple(R.raw.koala_bear, koala_bear, "Koala Bear", MediaPlayer.create(this, R.raw.koala_sound)),
                Quadruple(R.raw.lion, lion, "Lion", MediaPlayer.create(this, R.raw.lion_sound)),
                Quadruple(R.raw.reindeer, reindeer, "Reindeer", MediaPlayer.create(this, R.raw.deer_sound)),
                Quadruple(R.raw.wolverine, wolverine, "Wolverine", MediaPlayer.create(this, R.raw.wolverine_sound))
        ).forEachIndexed { index, quadruple ->
            ModelRenderable
                    .builder()
                    .setSource(this, quadruple.first).build()
                    .thenAccept {
                        val arObject = ARObject(quadruple.first, it, quadruple.second, quadruple.third, quadruple.fourth)
                        arObject.view.setOnClickListener(this@ArActivity)
                        arData[index] = arObject
                    }
                    .exceptionally {
                        Toast.makeText(this@ArActivity, "Unable to load model ${quadruple.first}", Toast.LENGTH_LONG).show()
                        null
                    }
        }
    }
}
