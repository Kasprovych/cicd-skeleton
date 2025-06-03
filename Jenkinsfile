pipeline {
    agent any
      environment {
        // Set environment variables for project and cluster
        PROJECT_ID    = 'your-gcp-project-id'
        GCR_REPO      = "gcr.io/${PROJECT_ID}/springboot-app"  // image repo in GCR
        CLUSTER_NAME  = 'your-gke-cluster'
        CLUSTER_ZONE  = 'us-central1-a'         // or your cluster zone/region
        GCLOUD_PATH =  '/Users/rkasprovych/Downloads/google-cloud-sdk/bin'
        DOCKER_PATH =  '/usr/local/bin'
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
         stage('Build docker Image') {
                        steps {
                           sh '$DOCKER_PATH/docker build -t us-central1-docker.pkg.dev/r-level-booking-service-461711/booxiwi-repo/springboot-app:v1 --platform linux/amd64 .'

                        }
                    }
          stage('Auth to GCP & GAR') {
                steps {
                        withCredentials([file(credentialsId: 'sa', variable: 'GCLOUD_KEY')]) {
                          sh '''
                            # Activate service-account credentials
                            $GCLOUD_PATH/gcloud auth activate-service-account --key-file="$GCLOUD_KEY" --project="r-level-booking-service-461711"
                            $GCLOUD_PATH/gcloud config set project r-level-booking-service-461711
                            cat $GCLOUD_KEY | $DOCKER_PATH/docker login -u _json_key --password-stdin https://us-central1-docker.pkg.dev
                            $DOCKER_PATH/docker push us-central1-docker.pkg.dev/r-level-booking-service-461711/booxiwi-repo/springboot-app:v1
                          '''
                        }
                      }
                    }


    stage('Deploy to cloud run') {
        steps {
            sh '''
                   $GCLOUD_PATH/gcloud run deploy test-run \
                     --image="us-central1-docker.pkg.dev/r-level-booking-service-461711/booxiwi-repo/springboot-app:v1" \
                     --platform=managed \
                     --allow-unauthenticated \
                     --concurrency=40 \
                     --memory=512Mi \
                     --region=us-central1 \
                     --timeout=300s
                 '''
        }
    }


  }

}