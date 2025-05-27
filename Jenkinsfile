pipeline {
      agent {
            docker {
                image 'maven:3.9.9-eclipse-temurin-21'
                args '-v $HOME/.m2:/root/.m2'
            }
      }

      environment {
        // Set environment variables for project and cluster
        PROJECT_ID    = 'your-gcp-project-id'
        GCR_REPO      = "gcr.io/${PROJECT_ID}/springboot-app"  // image repo in GCR
        CLUSTER_NAME  = 'your-gke-cluster'
        CLUSTER_ZONE  = 'us-central1-a'         // or your cluster zone/region
        GCLOUD_KEY    = credentials('sa')
        // ^ Jenkins credential (type "Secret file") for the GCP service account JSON
    }
    stages {
        stage('Test') {
            steps {
              sh '././mvnw test'
            }
        }
         stage('Build') {
                    steps {
                        sh './mvnw -B -DskipTests clean package'
                    }
                }
                   stage('Auth to GCP & GAR') {
                      steps {
                        withCredentials([file(credentialsId: 'sa', variable: 'GCLOUD_KEY')]) {
                          sh '''
                            # Activate service-account credentials
                            gcloud auth activate-service-account --key-file="$GCLOUD_KEY" --project="r-level-booking-service"

                            # Teach Docker to use those creds for Artifact Registry
                            gcloud auth configure-docker central1-docker.pkg.dev --quiet
                          '''
                        }
                      }
                    }

    stage('Build docker Image') {
        steps {
        // sh "docker login -u _json_key -p \"$(cat $GCLOUD_KEY)\" https://gcr.io"
           sh 'docker build -t us-central1-docker.pkg.dev/r-level-booking-service/booxiwi-repo/springboot-app:v1 --platform linux/amd64 .'
           sh 'docker push us-central1-docker.pkg.dev/r-level-booking-service/booxiwi-repo/springboot-app:v1'
        }
}


    }

}