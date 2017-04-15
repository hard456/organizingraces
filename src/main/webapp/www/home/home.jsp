<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    <jsp:body>

        <!-- Carousel -->

        <div id="carousel" style="width: auto; height: auto;">
            <div style="width: 100%; height: auto; background-color: whitesmoke; padding: 0; margin: 0;">
                <div id="myCarousel" class="carousel slide hidden-xs" data-ride="carousel"
                     style="padding: 10px 0 10px 0">
                    <!-- Indicators -->
                    <ol class="carousel-indicators">
                        <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                        <li data-target="#myCarousel" data-slide-to="1"></li>
                        <li data-target="#myCarousel" data-slide-to="2"></li>
                    </ol>

                    <div class="carousel-inner" role="listbox" style="height: 500px;">

                        <div class="item active"
                             style="text-align: center; line-height: 500px; vertical-align: middle;">
                            <span class="carousel-inside">Create your orienteering race</span>
                        </div>

                        <div class="item" style="text-align: center; line-height: 500px; vertical-align: middle;">
                            <span class="carousel-inside" style="background: orange">Manage your orienteering race</span>
                        </div>

                        <div class="item" style="text-align: center; line-height: 500px; vertical-align: middle;">
                            <span class="carousel-inside" style="background: #ff4ec9">Resolve your orienteering race</span>
                        </div>

                    </div>

                    <!-- Left and right controls -->
                    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                        <span class="sr-only">Previous</span>
                    </a>
                    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                        <span class="sr-only">Next</span>
                    </a>
                </div>
            </div>
        </div>
        <div class="container-fluid" style="background-color: #D8DBE8;">
            <div class="container" style="margin: 30px auto 0 auto; max-width: 1000px;">

                <div class="jumbotron" style="background-color: white;">
                    <div class="row">
                        <div class="col-xs-12 col-md-9" style="text-align: center; color: slategrey;">
                            <h3>Only registered user has permission to create events!</h3>
                        </div>
                        <div class="col-xs-12 col-md-3" style="text-align: center; margin-top: 15px;">
                            <a href="${pageContext.request.contextPath}/registration">
                                <button type="button" class="btn btn-danger">Registration</button>
                            </a>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-xs-12 col-md-4" style="text-align: center;">
                        <div class="jumbotron" style="background-color: white;">
                            <span class="glyphicon glyphicon-floppy-saved"
                                  style="font-size:2em; color: slategrey;"></span><br>
                            <span style="font-size: 15px; color: slategrey;">Export data to excel</span>
                        </div>
                    </div>
                    <div class="col-xs-12 col-md-4" style="text-align: center;">
                        <div class="jumbotron" style="background-color: white;">
                            <span class="glyphicon glyphicon-sort-by-attributes" style="font-size:2em; color: coral;"></span><br>
                            <span style="font-size: 15px; color: slategrey;">Sort and filter results</span>
                        </div>
                    </div>
                    <div class="col-xs-12 col-md-4" style="text-align: center;">
                        <div class="jumbotron" style="background-color: white;">
                            <span class="glyphicon glyphicon-upload"
                                  style="font-size:2em; color: dodgerblue;"></span><br>
                            <span style="font-size: 15px; color: slategrey;">Upload data from Excel</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:template>