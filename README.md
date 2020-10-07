# ituglib-build

## Build scripts for ITUGLIB pipelines

This project contains files associated with the ITUGLIB build system in
Jenkins.

The Jenkinsfile files are complete pipelines that can just be dropped into
new or existing Pipeline projects in Jenkins. No other configuration is
typically required for the pipeline. Just create a new pipeline project and
paste the file contents into the Pipeline script box.

- Jenkinsfile - the project for automatically picking up ITUGLIB.
- Jenkinsfile.deploy.* - deployment scripts for each product (HPITUG only).
- Jenkinsfile.*.hpitug - project specific to HPITUG
- Jenkinsfile.*.cgnac1 - project specific to CGNAC1

- *.dist - descriptor files used by the Jenkinsfile projects to figure out
         how to compute the version being built. This varies by product.
- *.bin.list - Manifest approach to constructing binary distribution list.
             This has been replaced with installing to ${WORKSPACE}/install
             and then taking all files from there into a tar file.
             (Deprecated)
- dist.info* - infrastructure for version computation
- *.properties - infrastructure for builds (Somewhat deprecated).
- *.groovy - Groovy script packages for the Jenkin Stage structure. This may
           be redone based on moving to full pipelines in the Jenkins Stage
           instance.
