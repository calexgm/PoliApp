package com.example.poliapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.shapes.Shape
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class buttons : Fragment() {

    companion object {
        fun newInstance() = buttons()
    }

    private lateinit var viewModel: ButtonsViewModel
    private lateinit var imageShape: ImageView
    private lateinit var shapeBitmap: Bitmap
    private lateinit var canvas: Canvas
    private val paint = Paint()
    private var currentColor = Color.GRAY
    private var currentShape: Shape= Shape.SQUARE
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buttons, container, false)
        imageShape = view.findViewById(R.id.image_shape)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        shapeBitmap = Bitmap.createBitmap(130, 130, Bitmap.Config.ARGB_8888)
        canvas = Canvas(shapeBitmap)

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        imageShape.setImageBitmap(shapeBitmap)

        val btnSquare = view?.findViewById<Button>(R.id.btn_square)
        btnSquare?.setOnClickListener {
            currentShape = Shape.SQUARE
            drawShape()
        }
        val btnCircle = view?.findViewById<Button>(R.id.btn_circle)
        btnCircle?.setOnClickListener {
            currentShape = Shape.CIRCLE
            drawShape()
        }
        val btnTriangle = view?.findViewById<Button>(R.id.btn_triangle)
        btnTriangle?.setOnClickListener {
            currentShape = Shape.TRIANGLE
            drawShape()
        }
        val btnRectangle = view?.findViewById<Button>(R.id.btn_rectangle)
        btnRectangle?.setOnClickListener {
            currentShape = Shape.RECTANGLE
            drawShape()
        }
        val btnStar = view?.findViewById<Button>(R.id.btn_star)
        btnStar?.setOnClickListener {
            currentShape = Shape.STAR
            drawShape()
        }
        val btnOval = view?.findViewById<Button>(R.id.btn_oval)
        btnOval?.setOnClickListener {
            currentShape = Shape.OVAL
            drawShape()
        }
        //Colores

        val btnAmarillo = view?.findViewById<Button>(R.id.btn_amarillo)
        btnAmarillo?.setOnClickListener {
            currentColor = Color.YELLOW
            changeColor()
        }

        val btnAzul = view?.findViewById<Button>(R.id.btn_azul)
        btnAzul?.setOnClickListener {
            currentColor = Color.BLUE
            changeColor()
        }

        val btnRojo = view?.findViewById<Button>(R.id.btn_rojo)
        btnRojo?.setOnClickListener {
            currentColor = Color.RED
            changeColor()
        }

        val btnVerde = view?.findViewById<Button>(R.id.btn_verde)
        btnVerde?.setOnClickListener {
            currentColor = Color.GREEN
            changeColor()
        }

        val btnMorado = view?.findViewById<Button>(R.id.btn_morado)
        btnMorado?.setOnClickListener {
            currentColor = Color.MAGENTA
            changeColor()
        }

        val btnNegro = view?.findViewById<Button>(R.id.btn_negro)
        btnNegro?.setOnClickListener {
            currentColor = Color.BLACK
            changeColor()
        }
    }
    enum class Shape {
        SQUARE, CIRCLE, TRIANGLE, RECTANGLE, STAR, OVAL
    }

    private fun drawSquare() {
        val centerX = shapeBitmap.width / 2f
        val centerY = shapeBitmap.height / 2f
        val size = shapeBitmap.width * 0.8f / 2f
        val rect = RectF(centerX - size, centerY - size, centerX + size, centerY + size)
        canvas.drawRect(rect, paint)
        imageShape.invalidate()
    }

    private fun drawCircle() {
        val centerX = shapeBitmap.width / 2f
        val centerY = shapeBitmap.height / 2f
        val radius = shapeBitmap.width * 0.4f
        canvas.drawCircle(centerX, centerY, radius, paint)
        imageShape.invalidate()
    }

    private fun drawTriangle() {
        val path = Path()
        val centerX = shapeBitmap.width / 2f
        val centerY = shapeBitmap.height / 2f
        val size = shapeBitmap.width * 0.8f / 2f

        path.moveTo(centerX, centerY - size) // Punto superior
        path.lineTo(centerX - size * sqrt(3f) / 2f, centerY + size / 2f) // Punto inferior izquierdo
        path.lineTo(centerX + size * sqrt(3f) / 2f, centerY + size / 2f) // Punto inferior derecho
        path.close()

        canvas.drawPath(path, paint)
        imageShape.invalidate()
    }

    private fun drawRectangle() {
        val rect = RectF(5f, 30f, shapeBitmap.width.toFloat() - 20f, shapeBitmap.height.toFloat() - 20f)
        canvas.drawRect(rect, paint)
        imageShape.invalidate()
    }

    private fun drawStar() {
        val path = Path()
        val centerX = shapeBitmap.width / 2f
        val centerY = shapeBitmap.height / 2f
        val outerRadius = shapeBitmap.width * 0.4f
        val innerRadius = shapeBitmap.width * 0.2f

        for (i in 0 until 5) {
            val angle = Math.toRadians((i * 72).toDouble())
            val x = centerX + outerRadius * cos(angle).toFloat()
            val y = centerY + outerRadius * sin(angle).toFloat()
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            val innerAngle = Math.toRadians((i * 72 + 36).toDouble())
            val innerX = centerX + innerRadius * cos(innerAngle).toFloat()
            val innerY = centerY + innerRadius * sin(innerAngle).toFloat()
            path.lineTo(innerX, innerY)
        }

        path.close()
        canvas.drawPath(path, paint)
        imageShape.invalidate()
    }

    private fun drawOval() {
        val rect = RectF(20f, 35f, shapeBitmap.width.toFloat() - 20f, shapeBitmap.height.toFloat() - 20f)
        canvas.drawOval(rect, paint)
        imageShape.invalidate()
    }

    private fun clearCanvas() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        imageShape.invalidate()

        paint.isAntiAlias = true
        paint.color = currentColor
        paint.style = Paint.Style.FILL
    }

    private fun changeColor(){
        paint.color = currentColor
        paint.style = Paint.Style.FILL
        drawShape()
    }

    private fun drawShape() {
        clearCanvas()
        when (currentShape) {
            Shape.SQUARE -> drawSquare()
            Shape.CIRCLE -> drawCircle()
            Shape.TRIANGLE -> drawTriangle()
            Shape.RECTANGLE -> drawRectangle()
            Shape.STAR -> drawStar()
            Shape.OVAL -> drawOval()
        }
    }


}