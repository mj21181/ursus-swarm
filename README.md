# ursus-swarm

A Java library for robot-based path planning and particle swarm optimization.

This library was created as part of the work for my Master's Thesis. If you are interested, it is freely available for download here:
http://search.proquest.com/docview/1868839549

Why name the library ursus-swarm? I could have named it JSwarm, Java Swarm, or some Swarm-Java combination.  While functionally descriptive I think those names are boring. A swarm of bears though? That's a terrifying prospect.

Project Structure
------------------

*lib/* - Folder containing dependent libraries and jars. In the future, this will hopefully be replaced by Maven <br>
*resources/* - Folder containing project resources like images <br>
*src/* - Folder containing source files <br>
<br>
*src/main/java/* - Main source files <br>
*src/test/java/* - Unit Tests <br>

src/main/java:

*event/* - Source files associated with transmission, reception, and processing of events and messages passed between robots <br>
*gui/* - Source files associated with various GUI components like the display of plots <br>
*log/* - Source files related to the library's logging mechanism. Interface exists for attaching log messages to an application using this library <br>
*path/* - Source files related to robot path planning for single or multiple robots <br>
*pso/* - Source files related to particle swarm optimization robots can use to search for some goal <br>
*render/* - Source files associated with a renderer used to render simulations or logs <br>
*task/* - Source files associated with multitasking and Thread synchronization between robots <br>

Examples
===================

Path Planning
---------------

TODO
