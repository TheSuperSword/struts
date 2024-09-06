pipeline {
    agent any
    stages {
        stage('Print Environment') {
            steps {
                sh 'printenv' // Linux/Unix
                bat 'set'     // Windows
            }
        }
    }
}