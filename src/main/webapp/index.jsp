<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<html5>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
    <title>JDBC Project</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <style>
      body {
        padding: 5vh 70vw 5vh 5vw;
        background-color: rgb(216, 216, 216);
      }
      label {
        margin-right: 10px;
      }
    </style>
  </head>
  <!-- <%@ page import="java.util.Date" %> -->
  <body>
    <form id="filterForm" method="post" action="https://cs450-jdbc.herokuapp.com/search">
      <div class="form-group form-inline">
        <label for="author">Author: </label>
        <input type="text" class="form-control" id="author" name="author">
      </div>
      <div class="form-group form-inline">
        <label for="title">Title: </label>
        <input type="text" class="form-control" id="title" name="title">
      </div>
      <div class="form-group form-inline">
        <label for="year">Year: </label>
        <input type="text" class="form-control" id="year" name="year">
      </div>
      <div class="form-group form-inline">
        <label for="type">Type: </label>
        <select id="type" class="form-control">
          <option value="short" selected>short</option>
          <option value="long">long</option>
        </select>
      </div>
      <div class="form-group form-inline">
        <label for="sort">Sorted By: </label>
        <select id="sort" class="form-control">
          <option value="Author" selected>author of publication</option>
          <option value="Title">title of publication</option>
          <option value="Year">year of publication</option>
        </select>
      </div>
      <div class="form-group form-inline">
        <label for="limit">Record Per page: </label>
        <select id="limit" class="form-control">
          <option value="10" selected>10</option>
          <option value="25">25</option>
          <option value="50">50</option>
        </select>
      </div>
      <button class="btn btn-primary">Search </button>
    </form>
    <br>
    <p> OR </p>
    <br>
    <form id="idForm" method="post" action="https://cs450-jdbc.herokuapp.com/search">
      <div class="form-group form-inline">
        <label for="pubId">Enter PublicationID: </label>
        <input type="text" class="form-control" id="pubId" name="pubId">
      </div>
      <button class="btn btn-primary">Search </button>
    </form>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
    <script>
      var servletURL = window.location.origin;
    </script>
  </body>
</html>
