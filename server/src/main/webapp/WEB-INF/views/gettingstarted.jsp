<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
    $(document).ready(function () {
        prettyPrint();
    });
</script>

<div class="container">
    <div class="hero-unit">
        <h1>Getting started</h1>

        <p>and code samples</p>
    </div>

    <div class="row">
        <div class="span6">
            <div class="well well-large">
                <h2>Getting started with the JavaScript client</h2>

                <p>We have prepared a client that makes it really easy and quick to get started. </p>
                <p>Start off by cloning the repo:</p>
                <pre class="prettyprint">
git clone https://github.com/cygni/texas-holdem-client-javascript</pre>
                <p>
                    The poker server runs as a docker process. The clients communicates with the server via
                    sockets on port 4711 and there is a web interface on port 80 (and it is mapped to
                    localhost on port 8080).
                </p>
                <p>Start the server via docker-compose:</p>
                <pre class="prettyprint">
docker-compose up poker-server</pre>
                <p>
                    Now you can browse the admin interface on <a href="http://localhost:8080">http://localhost:8080</a>.
                </p>
                <p>
                    Next up, getting the client started. Open a new terminal and start the development shell by executing this:
                </p>
                <pre class="prettyprint">
docker-compose run --rm poker-shell</pre>
                <p>
                    This starts a terminal where the project root folder is mapped as a volume. In this shell you have all the tools needed to run the client. Start off by installing all libs:
                </p>
                <pre class="prettyprint">
yarn install</pre>
                <p>
                    Then start the client against your local poker-server:
                </p>
                <pre class="prettyprint">
yarn play:local:training JohnnyPuma</pre>
                <p>
                    The `JohnnyPuma`-part may look a bit odd but it is simply the name of the poker player.
                </p>
                <p>
                    The code for your bot is placed in the folder named `my-bot`.
                </p>
                <p>
                    So, there are three rooms – `training`, `freeplay`, and finally the `tournament` room. The
                    `training` room is where you typically play when you develop your bot. The `tournament` is
                    the where you meet other bots in a real tournament. In the `freeplay` room you can practice
                    against other bots in a tournament-like style.
                </p>
                <p>
                    There are two servers configured for the client. The local version that is mentioned above and
                    the online version that is hosted on [http://poker.cygni.se](http://poker.cygni.se).
                </p>
                <p>
                    The following start commands are available:
                </p>
                <pre class="prettyprint">
yarn play:local:training
yarn play:local:freeplay
yarn play:local:tournament
yarn play:online:training
yarn play:online:freeplay
yarn play:online:tournament</pre>
                <p>
                    Note that the player name must be provided as an argument after the `yarn`-command.
                </p>
                <p>
                    To start creating your own bot get hacking on the file index.mjs in the subdirectory /my-bot!
                </p>
                <p>
                    Read more at: <a href="https://github.com/cygni/texas-holdem-client-javascript">https://github.com/cygni/texas-holdem-client-javascript</a>
                </p>

            </div>
        </div>
        <!--/span-->

        <div class="span6">
            <div class="well well-large">
                <h2>Prerequisites</h2>

                <p>You only need three things to get started!</p>
                <p>
                    <a href="https://hub.docker.com/?overlay=onboarding">Docker</a><br/>
                    <a href="https://git-scm.com/">git</a>
                </p>
                <p>
                    Note that to install Docker for Windows or MacOS you need to create/have an account at dockerhub.
                    It's free and quick to setup.
                </p>
                <p>
                    A good JavaScipt editor is nice to have! <a href="https://code.visualstudio.com/">Visual Studio Code</a> is great.
                </p>
            </div>
            <!--/span-->
        </div>

    </div>

    <!--/span-->
    <!--/row-->
</div>
<!--/.fluid-container-->
