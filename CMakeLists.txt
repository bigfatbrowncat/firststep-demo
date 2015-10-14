cmake_minimum_required (VERSION 2.8)

# Title
set(PROJNAME "firststep-demo")
project (${PROJNAME})

# Native code


# Java code

# 	J1. Titles
set(JARNAME ${PROJNAME})
find_package(Java REQUIRED)
include(cmake_patches/UseJavaCustom.cmake)

# 	J2. Compiler options
set(CMAKE_JAVA_COMPILE_FLAGS "-source" "1.7" "-target" "1.7")
set(CMAKE_JAVA_INCLUDE_PATH "${CMAKE_BINARY_DIR}/firststep/firststep-java.jar")

# 	J3. Sources and java resources
set(CMAKE_CURRENT_SOURCE_DIR "${CMAKE_CURRENT_SOURCE_DIR}/src/java")
file(GLOB_RECURSE SOURCES_JAVA "*.java")
file(GLOB_RECURSE RESOURCES_TTF RELATIVE "${CMAKE_CURRENT_SOURCE_DIR}" "${CMAKE_CURRENT_SOURCE_DIR}/*.ttf")
file(GLOB_RECURSE RESOURCES_PNG RELATIVE "${CMAKE_CURRENT_SOURCE_DIR}" "${CMAKE_CURRENT_SOURCE_DIR}/*.png")

# 	J4. Linking
add_jar(${JARNAME} ${SOURCES_JAVA} ${RESOURCES_TTF} ${RESOURCES_PNG})

#	J5 (optional). Printing out the paths
get_target_property(_jarFile ${JARNAME} JAR_FILE)
get_target_property(_classDir ${JARNAME} CLASSDIR)
message(STATUS "[${PROJNAME}] Jar file: ${_jarFile}")
message(STATUS "[${PROJNAME}] Classes compiled to: ${_classDir}")

# Custom tasks

if (MINGW)
	set(
		CMD "${CMAKE_CURRENT_SOURCE_DIR}/../../run-mingw.sh"
	)
else (MINGW)
	set(
		CMD "${CMAKE_CURRENT_SOURCE_DIR}/../../run-unix.sh"
	)
endif (MINGW)

message(STATUS "CMD: ${CMD}")

add_custom_target(
	run 
	COMMAND ${CMD} ${Java_JAVA_EXECUTABLE}
)

# Dependencies

#	D1. Subprojects (with flags)
add_subdirectory(firststep)

#	D2. Target dependencies
add_dependencies(${PROJNAME} firststep)
add_dependencies(run ${JARNAME})
