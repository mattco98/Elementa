package com.example.examplemod

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.components.Window
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.constraints.animation.LinearStrategy
import club.sk1er.elementa.features.ScissorFeature
import club.sk1er.elementa.helpers.Padding
import net.minecraft.client.gui.GuiScreen
import java.awt.Color

class ExampleGui : GuiScreen() {
    private val settings: UIComponent

    var offset = 5f

    init {
        Window.clearChildren()

        settings = UIBlock(Color(0, 172, 193, 255))
        settings.getConstraints()
                .setX(PixelConstraint(5f))
                .setY(PixelConstraint(5f))
                .setWidth(RelativeConstraint(0.3f))
                .setHeight(FillConstraint(-5f))

        val top = UIBlock(Color(0, 124, 145, 255))
        top.getConstraints()
                .setWidth(FillConstraint())
                .setHeight(PixelConstraint(20f))

        val title = UIText("Settings")
        title.getConstraints()
                .setX(PixelConstraint(5f))
                .setY(CenterConstraint())

        val button = UIBlock(Color(93, 222, 244, 255))
        button.getConstraints()
                .setX(PixelConstraint(5f, true))
                .setY(PixelConstraint(5f))
                .setWidth(PixelConstraint(10f))
                .setHeight(PixelConstraint(10f))
        button.onClick {
            Window.removeChild(settings)
            println("CLICKED")
        }

        top.addChild(button)
        top.addChild(title)

        settings.addChild(top)
        settings.addChild(createSlider("Slider 1", PixelConstraint(30f)))
        settings.addChild(createSlider("Second Slider", PixelConstraint(65f)))

        val blocky = UIBlock(Color.RED)
        blocky.getConstraints()
                .setX(SiblingConstraint(Padding(2f)))
                .setY(SiblingConstraint(Padding(5f)))
                .setWidth(PixelConstraint(75f))
                .setHeight(PixelConstraint(35f))

        val blocky2 = UIBlock(Color.GREEN)
        blocky2.getConstraints()
                .setX(CramSiblingConstraint(Padding(5f)))
                .setY(CramSiblingConstraint(Padding(5f)))
                .setWidth(PixelConstraint(75f))
                .setHeight(PixelConstraint(15f))

        val blocky3 = UIBlock(Color.GRAY)
        blocky3.getConstraints()
                .setX(CramSiblingConstraint(Padding(5f)))
                .setY(CramSiblingConstraint(Padding(5f)))
                .setWidth(PixelConstraint(15f))
                .setHeight(PixelConstraint(15f))

        val cont = UIContainer()
        cont.getConstraints()
                .setHeight(ChildBasedSizeConstraint())
                .setWidth(FillConstraint())
                .setX(SiblingConstraint())
                .setY(SiblingConstraint())
        cont.addChildren(blocky, blocky2, blocky3)

        settings.addChild(cont)

        val textHolder = UIBlock(Color.PINK)
        textHolder.getConstraints()
            .setX(CenterConstraint())
            .setY(CenterConstraint())
            .setWidth(PixelConstraint(50f))
            .setHeight(PixelConstraint(10f))

        val bigText = UIText("I'm longer than my container")
        textHolder.addChild(bigText)

        textHolder.enableFeatures(ScissorFeature())

        Window.addChild(settings)
        Window.addChild(textHolder)

//        val image = UIImage("./images/logo.png", "https://avatars3.githubusercontent.com/u/10331479?s=460&v=4")
//        image.getConstraints()
//                .setX(CenterConstraint())
//                .setY(PixelConstraint(3f))
//                .setWidth(RelativeConstraint(0.2f))
//                .setHeight(AspectConstraint(1f))
//
//        Window.addChild(image);
    }


    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)

        if (mouseButton == 1) {
            offset += 35

            val anim = settings.makeAnimation()
                .setXAnimation(LinearStrategy, 5f, PixelConstraint(offset))

            settings.animateTo(anim)
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        Window.draw()
    }

    private fun createSlider(text: String, yConstraint: PositionConstraint): UIComponent {
        val container = UIContainer()
        container.getConstraints()
                .setX(PixelConstraint(15f))
                .setY(yConstraint)
                .setWidth(FillConstraint(-15f))
                .setHeight(PixelConstraint(30f))

        val title = UIText(text)

        val back = UIBlock(Color(64, 64, 64, 255))
        back.getConstraints()
                .setY(PixelConstraint(12f))
                .setWidth(FillConstraint())
                .setHeight(PixelConstraint(5f))

        val grab = UIBlock(Color(0, 0, 0, 255))
        grab.getConstraints()
                .setX(PixelConstraint(0f))
                .setY(PixelConstraint(-2f))
                .setWidth(PixelConstraint(3f))
                .setHeight(PixelConstraint(9f))

        container.addChildren(title, back.addChild(grab))

        return container
    }
}