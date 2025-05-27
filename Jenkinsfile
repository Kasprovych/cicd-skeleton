pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-21'
            args  '-v $HOME/.m2:/root/.m2'   // кешує залежності
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
    }
}