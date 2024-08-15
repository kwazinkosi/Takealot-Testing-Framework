pipeline {
  agent any  // Run on any available Jenkins agent

 tools {
     maven 'Maven'
  }

  
  stages {
    stage('Build') {
      steps {
      	bat "mvn --version"
      	script{
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
        bat"mvn clean"
        echo 'cleaning!'
      }
    }
  }
}
