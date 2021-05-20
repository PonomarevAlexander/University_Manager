<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x"
	crossorigin="anonymous">
<title>University</title>
</head>

<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-warning">
		<div class="container-fluid">
			<a class="navbar-brand" href="#">University</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
				aria-controls="navbarSupportedContent" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="/university-manager">Home</a></li>
					<li><a class="nav-link active" aria-current="page"
						href="/university-manager/departments">Departments</a></li>
					<li><a class="nav-link active" aria-current="page"
						href="/university-manager/groups">Groups</a></li>
					<li><a class="nav-link active" aria-current="page"
						href="/university-manager/students">Students</a></li>
					<li><a class="nav-link active" aria-current="page"
						href="/university-manager/teachers">Teachers</a></li>
					<li><a class="nav-link active" aria-current="page"
						href="/university-manager/lessons">Lessons</a></li>
					<li><a class="nav-link active" aria-current="page"
						href="/university-manager/timetables">Timetables</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<br/>
	<h3 class="text-center">Main</h3>
</body>
</html>