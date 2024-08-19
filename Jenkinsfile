pipeline {
  agent any  // Run on any available Jenkins agent

  tools {
    maven 'Maven'
  }

  stages {
    stage('Setup Custom JARs') {
      steps {
        script {
          bat "mvn install:install-file -Dfile=libs/wait-utils-0.0.1-SNAPSHOT.jar -DgroupId=com.kwazi -DartifactId=wait-utils -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DgeneratePom=true"
          bat "mvn install:install-file -Dfile=libs/file-utils-0.0.1-SNAPSHOT.jar -DgroupId=com.kwazi -DartifactId=file-utils -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DgeneratePom=true"
          bat "mvn install:install-file -Dfile=libs/logs-report-util-0.0.1-SNAPSHOT.jar -DgroupId=com.kwazi -DartifactId=logs-report-util -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DgeneratePom=true"
        }
      }
    }
    
    stage('Build') {
      steps {
        bat "mvn --version"
        script {
          git branch: 'TakealotAutomation', credentialsId: '2e4b2a17-3186-48fb-a89a-300eb1fb4bce', 
          url: 'https://git.nagarro.com/GITG00641/Automation-Manual_QA/3213851.git'
          bat "mvn clean compile"
          echo 'Build RUNNING!'
        }
      }
    }
    
    stage('Test') {
      steps {
        // Execute unit tests 
        bat "mvn test"
        echo 'Unit tests RUNNING!'
        
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
            //bat 'mvn clean test'
        }
      }
    }
    
    stage('Cleanup') {
      steps {
        // Clean up temporary files 
        bat "mvn clean"
        echo 'cleaning!'
      }
    }
  }
}
