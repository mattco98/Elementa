package gg.essential.elementa.constraints

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.resolution.ConstraintVisitor

/**
 * Sets this component's width or height to be the sum of its children's width or height
 */
class ChildBasedSizeConstraint(val padding: Float = 0f) : SizeConstraint {
    override var cachedValue = 0f
    override var recalculate = true
    override var constrainTo: UIComponent? = null

    override fun getWidthImpl(component: UIComponent): Float {
        val holder = (constrainTo ?: component)
        return holder.children.sumOf {
            it.getWidth() + ((it.constraints.x as? PaddingConstraint)?.getHorizontalPadding(it) ?: 0f).toDouble()
        }.toFloat() + (holder.children.size - 1) * padding
    }

    override fun getHeightImpl(component: UIComponent): Float {
        val holder = (constrainTo ?: component)
        return holder.children.sumOf {
            it.getHeight() + ((it.constraints.y as? PaddingConstraint)?.getVerticalPadding(it) ?: 0f).toDouble()
        }.toFloat() + (holder.children.size - 1) * padding
    }

    override fun getRadiusImpl(component: UIComponent): Float {
        return (constrainTo ?: component).children.sumOf { it.getHeight().toDouble() }.toFloat() * 2f
    }

    override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {
        when (type) {
            ConstraintType.WIDTH -> visitor.visitChildren(ConstraintType.WIDTH)
            ConstraintType.HEIGHT -> visitor.visitChildren(ConstraintType.HEIGHT)
            ConstraintType.RADIUS -> visitor.visitChildren(ConstraintType.HEIGHT)
            else -> throw IllegalArgumentException(type.prettyName)
        }
    }
}

class ChildBasedMaxSizeConstraint : SizeConstraint {
    override var cachedValue = 0f
    override var recalculate = true
    override var constrainTo: UIComponent? = null

    override fun getWidthImpl(component: UIComponent): Float {
        return (constrainTo ?: component).children.maxByOrNull {
            it.getWidth() + ((it.constraints.x as? PaddingConstraint)?.getHorizontalPadding(it) ?: 0f)
        }?.getWidth() ?: 0f
    }

    override fun getHeightImpl(component: UIComponent): Float {
        return (constrainTo ?: component).children.maxByOrNull {
            it.getHeight() + ((it.constraints.y as? PaddingConstraint)?.getVerticalPadding(it) ?: 0f)
        }?.getHeight() ?: 0f
    }

    override fun getRadiusImpl(component: UIComponent): Float {
        return (constrainTo ?: component).children.maxByOrNull { it.getHeight() }?.getHeight()?.times(2f) ?: 0f
    }

    override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {
        when (type) {
            ConstraintType.WIDTH -> visitor.visitChildren(ConstraintType.WIDTH)
            ConstraintType.HEIGHT -> visitor.visitChildren(ConstraintType.HEIGHT)
            ConstraintType.RADIUS -> visitor.visitChildren(ConstraintType.HEIGHT)
            else -> throw IllegalArgumentException(type.prettyName)
        }
    }
}

// TODO: Is there a good way to calculate this number for radii, or should this just be an invalid constraint
//  for radius?
class ChildBasedRangeConstraint : WidthConstraint, HeightConstraint {
    override var cachedValue = 0f
    override var recalculate = true
    override var constrainTo: UIComponent? = null

    override fun getWidthImpl(component: UIComponent): Float {
        var leftMostPoint = Float.MAX_VALUE
        var rightMostPoint = Float.MIN_VALUE

        component.children.forEach {
            if (it.getLeft() < leftMostPoint) {
                leftMostPoint = it.getLeft()
            }

            if (it.getRight() > rightMostPoint) {
                rightMostPoint = it.getRight()
            }
        }

        return (rightMostPoint - leftMostPoint).coerceAtLeast(0f)
    }

    override fun getHeightImpl(component: UIComponent): Float {
        var topMostPoint = Float.MAX_VALUE
        var bottomMostPoint = Float.MIN_VALUE

        component.children.forEach {
            if (it.getTop() < topMostPoint) {
                topMostPoint = it.getTop()
            }

            if (it.getBottom() > bottomMostPoint) {
                bottomMostPoint = it.getBottom()
            }
        }

        return (bottomMostPoint - topMostPoint).coerceAtLeast(0f)
    }

    override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {
        when (type) {
            ConstraintType.WIDTH -> {
                visitor.visitChildren(ConstraintType.X)
                visitor.visitChildren(ConstraintType.WIDTH)
            }
            ConstraintType.HEIGHT -> {
                visitor.visitChildren(ConstraintType.Y)
                visitor.visitChildren(ConstraintType.HEIGHT)
            }
            else -> throw IllegalArgumentException(type.prettyName)
        }
    }
}