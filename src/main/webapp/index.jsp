<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<html5>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
    <title>JDBC Project</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
  </head>
  <!-- <%@ page import="java.util.Date" %> -->
  <body>
    <form id="filterForm" method="post" action="">
      <div class="form-group">
        <label for="author">Author: </label>
        <input type="text" class="form-control" id="author" name="author">
      </div>
      <div class="form-group">
        <label for="title">Title: </label>
        <input type="text" class="form-control" id="title" name="title">
      </div>
      <div class="form-group">
        <label for="year">Year: </label>
        <input type="text" class="form-control" id="year" name="year">
      </div>
      <div class="form-group">
        <label for="type">Type: </label>
        <select id="type" class="form-control">
          <option value="short" selected>short</option>
          <option value="long">long</option>
        </select>
      </div>
      <div class="form-group">
        <label for="sort">Sorted By: </label>
        <select id="sort" class="form-control">
          <option value="author" selected>author of publication</option>
          <option value="title">title of publication</option>
          <option value="year">year of publication</option>
        </select>
      </div>
      <div class="form-group">
        <label for="limit">Record Per page: </label>
        <select id="limit" class="form-control">
          <option value="10" selected>10</option>
          <option value="25">25</option>
          <option value="50">50</option>
        </select>
      </div>
      <button onclick="window.location.assign(servletURL+'/search');">Search </button>
    </form>
    <br>
    <p> OR </p>
    <br>
    <form id="filterForm" method="post" action="">
      <div class="form-group" class="form-control">
        <label for="pubId">Enter PublicationID: </label>
        <input type="text" id="pubId" name="pubId">
      </div>
      <button onclick="window.location.assign(servletURL+'/search');">Search </button>
    </form>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
    <script>
      var servletURL = window.location.origin;
    </script>
  </body>
</html>
