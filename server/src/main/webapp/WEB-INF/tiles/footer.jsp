<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<hr />

<footer>
  <div class="container">
    <p class="pull-right">
      Cygni Texas Hold'em v<spring:eval
        expression="@applicationProperties.getProperty('application.version')"
      />
    </p>

    <p>&copy; Cygni AB 2020</p>
  </div>
</footer>
