> docker build -t texasholdemserver .
> docker tag texasholdemserver:latest cygni/texasholdem:server-1.1.22
> docker login (user cygni)
> docker push cygni/texasholdem:server-1.1.22
> gcloud compute instances create-with-container texasholdem \

     --container-image docker.io/cygni/texasholdem:server-1.1.22

> gcloud compute instances update-container texasholdem \

     --container-image docker.io/cygni/texasholdem:server-1.1.22
