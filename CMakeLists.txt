cmake_minimum_required (VERSION 2.8)

add_subdirectory(firststep)

set(PROJNAME "firststep-demo")
set(JARNAME ${PROJNAME})

find_package(Java REQUIRED)
include(cmake_patches/UseJavaCustom.cmake)

set(CMAKE_JAVA_COMPILE_FLAGS "-source" "1.7" "-target" "1.7")
set(CMAKE_JAVA_INCLUDE_PATH "${CMAKE_BINARY_DIR}/firststep/firststep-java.jar")

set(CMAKE_CURRENT_SOURCE_DIR "${CMAKE_CURRENT_SOURCE_DIR}/src/java")

file(GLOB_RECURSE SOURCES_JAVA "*.java")
file(GLOB_RECURSE RESOURCES_TTF RELATIVE "${CMAKE_CURRENT_SOURCE_DIR}" "${CMAKE_CURRENT_SOURCE_DIR}/*.ttf")
file(GLOB_RECURSE RESOURCES_PNG RELATIVE "${CMAKE_CURRENT_SOURCE_DIR}" "${CMAKE_CURRENT_SOURCE_DIR}/*.png")

add_jar(${JARNAME} ${SOURCES_JAVA} ${RESOURCES_TTF} ${RESOURCES_PNG})

add_dependencies(${PROJNAME} firststep)

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
add_dependencies(run ${JARNAME})
