import java.security.MessageDigest
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.jobs.MultibranchWorkflowJob

class TestSebastianMPJBuilder extends JobBuilder {
MultibranchWorkflowJob job, DslFactory dslFactory, def projectData
    TestSebastianMPJBuilder() {
        super(job, dslFactory, projectData)
    }

    void gitHubOrganization(Map params=[:]) {
        job.branchSources {
            branchSource {
                source {
                    github {
                        id(md5sum(projectData.githubUrl))
                        repoOwner(projectData.repoOwner)
                        repository(projectData.repoName)
                        apiUri('https://api.github.com')
                        credentialsId(projectData.githubOrganizationsJenkinsCredentialsRefId)

                        traits {
                            cleanBeforeCheckoutTrait()
                        }
                    }
                }
            }
        }

        job.orphanedItemStrategy {
            discardOldItems {
                numToKeep(1)
            }
        }

        job.triggers {
            // Scan at most once per day
            cron('* 10 * * *')
        }
    }

    void jenkinsfilePath(String jenkinsfilePath) {
        job.configure {
            it / factory(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory') {
                scriptPath("jenkinsfile/sebastian_mpjb_test.jenkinsfile")
            }
        }
    }

    def md5sum(String input) {
        MessageDigest digest = MessageDigest.getInstance("MD5")
        digest.update(input.bytes)
        return new BigInteger(1, digest.digest()).toString(16).padLeft(32, '0')
    }
}
