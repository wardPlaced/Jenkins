def catroidorg = new JobsBuilder(this).gitHubOrganization({new CatroidData()})
def catroidroot = new JobsBuilder(this).pipeline({new CatroidData()})

catroidorg.job("SebastianTestCron") {
    htmlDescription(['Job tests MultibranchPipelineJobBuilder.groovy',
                     'This job runs the Pipeline defined in the Jenkinsfile inside of this directory under jenkinsfiles',
                     'The Pipeline should run the groovy file.'])

    // Workspace rights are needed to show the java files for code coverage.
    jenkinsUsersPermissions(Permission.JobRead, Permission.JobBuild, Permission.JobCancel, Permission.JobWorkspace)

    // allow anonymous users to see the results of PRs to fix their issues
    anonymousUsersPermissions(Permission.JobRead, Permission.JobBuild, Permission.JobCancel)

    gitHubOrganization()
    jenkinsfilePath('jenkinsfiles/*.jenkinsfile')
    //labelForDockerBuild('Emulator')
}

Views.basic(this, "Catroid", "Catroid/.+")
