Gradle conversion
=====================

**ToDo**

- Better organization for common sub project settings
x Enable filtering
x Hook in rebelGenerate, initLogDir etc in one of the build steps
- Logging for Jetty is misplaced
- Fix intellij and eclipse settings
- Common versioning
- Result artifact names (server.jar is not good)
- Add help and info task
- FindBugs, Emma, CheckStyle, PMD, Cobertura and Sonar plugins
- Reporting
- Site ála Maven reporting
- Release management with version increment, github tagging etc
- Organize common dependencies in lists, for example all Logging libs
- Jenkins integration with tasks to copy resources for deployment, downloads etc
- How to package non-java projects?
- Install common artefacts in maven-repo
- Figure out how to use profiles for different types of builds


Structure changes
====================

- Move client examples to separate folder
- Create new folder for: {integration-tests, performance-tests}
- Create new project for static web
- Add JSLint, CSS merge, JS minify etc to static web project
- Let the server only expose RESTful interface
- Let the server notify some things via websockets (tournament updates, current live game updates, server status etc)
- Breakout java serverbots to own projects under folder: serverbots