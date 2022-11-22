package com.coder.gateway.models

import com.coder.gateway.sdk.v2.models.ProvisionerJobStatus
import com.coder.gateway.sdk.v2.models.Workspace
import com.coder.gateway.sdk.v2.models.WorkspaceTransition

enum class WorkspaceAgentStatus(val label: String) {
    QUEUED("◍ Queued"), STARTING("⦿ Starting"), STOPPING("◍ Stopping"), DELETING("⦸ Deleting"),
    RUNNING("⦿ Running"), STOPPED("◍ Stopped"), DELETED("⦸ Deleted"),
    CANCELING("◍ Canceling action"), CANCELED("◍ Canceled action"), FAILED("ⓧ Failed");

    companion object {
        fun from(workspace: Workspace) = when (workspace.latestBuild.job.status) {
            ProvisionerJobStatus.PENDING -> QUEUED
            ProvisionerJobStatus.RUNNING -> when (workspace.latestBuild.transition) {
                WorkspaceTransition.START -> STARTING
                WorkspaceTransition.STOP -> STOPPING
                WorkspaceTransition.DELETE -> DELETING
            }

            ProvisionerJobStatus.SUCCEEDED -> when (workspace.latestBuild.transition) {
                WorkspaceTransition.START -> RUNNING
                WorkspaceTransition.STOP -> STOPPED
                WorkspaceTransition.DELETE -> DELETED
            }

            ProvisionerJobStatus.CANCELING -> CANCELING
            ProvisionerJobStatus.CANCELED -> CANCELED
            ProvisionerJobStatus.FAILED -> FAILED
        }
    }
}