> docker build -t texasholdemserver .
> docker tag texasholdemserver:latest cygni/texasholdem:server-1.1.23
> docker login (user cygni)
> docker push cygni/texasholdem:server-1.1.23

If deploying to a new instance:
> gcloud compute instances create-with-container texasholdem --container-image docker.io/cygni/texasholdem:server-1.1.23

If redploying:
(choose correct project first:)
> gcloud init
> gcloud compute instances update-container texasholdem --container-image docker.io/cygni/texasholdem:server-1.1.23


Push to maven repo:
> mvn deploy

Make sure you have correct settings in ```~./m2/settings.xml```
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">
   <servers>
      <server>
         <id>cygni</id>
         <username>admin</username>
         <password>you need the password. Ask Emil</password>
      </server>
   </servers>
</settings>
```