pipeline {
  agent none
  options {
    buildDiscarder logRotator(daysToKeepStr: '14', numToKeepStr: '10')
    timeout(80)
    disableConcurrentBuilds()
    skipStagesAfterUnstable()
    quietPeriod(30)
  }
  stages {
    stage('Prepare') {
      agent any
      stages {
        stage('Clean up') {
          steps {
            echo 'Cleaning workspace...'
          }
        }
      }
    }
    stage('JDK 21') {
      agent any
      tools {
        jdk 'jdk_21_latest'
        maven 'maven_3_latest'
      }
      environment {
        MAVEN_OPTS = "-Xmx1024m"
      }
      stages {
        stage('Test') {
          steps {
            echo 'Testing with JDK 21...'
          }
        }
      }
      post {
        always {
          echo 'Cleaning workspace after JDK 21...'
        }
      }
    }
    stage('JDK 17') {
      agent any
      tools {
        jdk 'jdk_17_latest'
        maven 'maven_3_latest'
      }
      environment {
        MAVEN_OPTS = "-Xmx1024m"
      }
      stages {
        stage('Test & Coverage') {
          steps {
            echo 'Testing & coverage with JDK 17...'
          }
        }
        stage('Code Quality') {
          when {
            anyOf {
              branch 'master'; branch 'release/struts-7-0-x'
            }
          }
          steps {
            echo 'Performing code quality checks...'
          }
        }
        stage('Build Source & JavaDoc') {
          when {
            branch 'release/struts-7-0-x'
          }
          steps {
            echo 'Building source & JavaDoc...'
          }
        }
        stage('Deploy Snapshot') {
          when {
            branch 'release/struts-7-0-x'
          }
          steps {
            echo 'Deploying snapshot...'
          }
        }
        stage('Upload nightlies') {
          when {
            branch 'release/struts-7-0-x'
          }
          steps {
            echo 'Uploading nightlies...'
          }
        }
      }
      post {
        always {
          echo 'Cleaning workspace after JDK 17...'
        }
      }
    }
  }
  post {
    failure {
      echo "Build failed. Sending failure notification..."
    }
    unstable {
      echo "Build unstable. Sending unstable notification..."
    }
    fixed {
      echo "Build fixed. Sending notification..."
    }
  }
}
