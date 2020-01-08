# Texas hold'em Botgame

This is a server/client system for letting programmed bots play poker against each other.

**Quickstart** &mdash; _Host your own server._

    git clone https://github.com/emilb/texas-holdem-botgame.git
    cd texas-holdem-botgame

    docker build -f Dockerfile-server -t texas .
    docker run -p 8080:8080 -p 4711:4711 texas

Open browser to: http://localhost:8080

**Quickstart** &mdash; _Developing your client_

    git clone https://github.com/emilb/texas-holdem-botgame.git
    cd texas-holdem-botgame

    docker build -f Dockerfile-client -t texas-client .
    docker run texas-client
