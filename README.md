# Texas hold'em Botgame

This is a server/client system for letting programmed bots play poker against each other.

**Quickstart** &mdash; _Run with Docker._
docker pull cygni/texasholdem:server-1.1.22.2
docker run cygni/texasholdem:server-1.1.22.2


**Quickstart** &mdash; _Build your own server with Docker._

    git clone https://github.com/cygni/texas-holdem-botgame.git
    cd texas-holdem-botgame

    docker build -f Dockerfile-server -t texas .
    docker run -p 8080:8080 -p 4711:4711 texas

Open browser to: http://localhost:8080

**Quickstart** &mdash; _Host your own server with local Java._

    git clone https://github.com/cygni/texas-holdem-botgame.git
    cd texas-holdem-botgame

    mvn clean install
    mvn jetty:run

Open browser to: http://localhost:8080

**Quickstart** &mdash; _Developing your client_

    git clone https://github.com/cygni/texas-holdem-botgame.git
    cd texas-holdem-botgame

    docker build -f Dockerfile-client -t texas-client .
    docker run texas-client

**Tests**
`mvn org.pitest:pitest-maven:mutationCoverage`