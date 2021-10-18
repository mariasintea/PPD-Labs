cmake_minimum_required(VERSION 3.17)
project(lab1_c__)

set(CMAKE_CXX_STANDARD 14)

add_executable(lab1_c__ main.cpp Static.cpp Static.h Dynamic.cpp Dynamic.h)

#set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -Wl,--stack,4194304000")