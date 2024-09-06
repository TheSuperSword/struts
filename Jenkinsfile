#!groovy

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

        }
        stage('Code Quality') {

        }
        stage('Build Source & JavaDoc') {

        }
        stage('Deploy Snapshot') {

        }
        stage('Upload nightlies') {

        }
      }
    }
  }
  post {
    // If this build failed, send an email to the list.
    failure {
      script {
        emailext(
            to: "chengweelee0000@gmail.com",
            recipientProviders: [[$class: 'DevelopersRecipientProvider']],
            from: "Mr. Jenkins <jenkins@builds.apache.org>",
            subject: "Jenkins job ${env.JOB_NAME}#${env.BUILD_NUMBER} failed",
            body: """
There is a build failure in ${env.JOB_NAME}.

Build: ${env.BUILD_URL}
Logs: ${env.BUILD_URL}console
Changes: ${env.BUILD_URL}changes

--
Mr. Jenkins
Director of Continuous Integration
"""
        )
      }
    }

    // If this build didn't fail, but there were failing tests, send an email to the list.
    unstable {
      script {
        emailext(
            to: "chengweelee0000@gmail.com",
            recipientProviders: [[$class: 'DevelopersRecipientProvider']],
            from: "Mr. Jenkins <jenkins@builds.apache.org>",
            subject: "Jenkins job ${env.JOB_NAME}#${env.BUILD_NUMBER} unstable",
            body: """
Some tests have failed in ${env.JOB_NAME}.

Build: ${env.BUILD_URL}
Logs: ${env.BUILD_URL}console
Changes: ${env.BUILD_URL}changes

--
Mr. Jenkins
Director of Continuous Integration
"""
        )
      }
    }

    // Send an email, if the last build was not successful and this one is.
    fixed {
      script {
        emailext(
            to: "chengweelee0000@gmail.com",
            recipientProviders: [[$class: 'DevelopersRecipientProvider']],
            from: 'Mr. Jenkins <jenkins@builds.apache.org>',
            subject: "Jenkins job ${env.JOB_NAME}#${env.BUILD_NUMBER} back to normal",
            body: """
The build for ${env.JOB_NAME} completed successfully and is back to normal.

Build: ${env.BUILD_URL}
Logs: ${env.BUILD_URL}console
Changes: ${env.BUILD_URL}changes

--
Mr. Jenkins
Director of Continuous Integration
"""
        )
      }
    }
  }
}