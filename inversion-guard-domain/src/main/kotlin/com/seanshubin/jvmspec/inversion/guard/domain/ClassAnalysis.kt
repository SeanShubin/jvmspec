package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.model.api.JvmClass

data class ClassAnalysis(
    val jvmClass: JvmClass,
    val methodAnalysisList: List<MethodAnalysis>
) {
    fun complexity(): Int {
        return methodAnalysisList.sumOf { it.complexity }
    }

    fun countProblems(): Int {
        val boundaryStaticInvocationCount = countStaticInvocations(InvocationType.BOUNDARY)
        val unknownStaticInvocationCount = countStaticInvocations(InvocationType.UNKNOWN)
        val potentiallyBoundaryInvocationCount = boundaryStaticInvocationCount + unknownStaticInvocationCount
        val nonBoundaryLogicCount = countNonBoundaryLogic()
        return if (potentiallyBoundaryInvocationCount > 0 && nonBoundaryLogicCount > 0) {
            potentiallyBoundaryInvocationCount
        } else if (unknownStaticInvocationCount > 0) {
            unknownStaticInvocationCount
        } else {
            0
        }
    }

    private fun countStaticInvocations(invocationType: InvocationType): Int {
        return methodAnalysisList.sumOf { it.countStaticInvocations(invocationType) }
    }

    private fun countNonBoundaryLogic(): Int {
        return methodAnalysisList.count { !it.isBoundaryLogic() }
    }
}
