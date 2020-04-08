# sipg

Sustainable Infrastructure Planning Game

## Introduction

This project demonstrates a co-simulation platform for strategic cross-sector infrastructure planning in a fictional design scenario. It includes three main player roles (agriculture, water, and energy) controlling four infrastructure sectors (agriculture, water, petroleum, and electricity). It uses IEEE Std. 1516-2010 High Level Architecture (HLA) for simulation interoperability.

## Pre-requisites

This project uses a Maven build file (`pom.xml`) to configure most external dependencies. In addition, you must have a compliant IEEE Std. 1516-2010 High Level Architecture (HLA) runtime infrastructure (RTI). Unfortunately, most open source implementations do not have sufficient required features (e.g. synchronization points and save/restore). SIPG has been tested with Pitch pRTI version 5.4.5.0 and the `pom.xml` file assumes the environment variable `PRTI_HOME` points to its installation directory.

## Running the Program

There are three separate classes to launch each player role: `AgriculturePlayer`, `EnergyPlayer`, and `WaterPlayer`. An optional fourth `NullPlayer` class does not control any infrastructure sectors but, by default, has greater visibility to top-level performance metrics. These classes will attempt to automatically connect to the RTI on launch.

Additionally, a `SuperPlayer` class includes all infrastructure sectors for testing. This class does not require an HLA implementation as there is only a single player and can be run as a standalone application.

## Acknowledgements

This project was funded, in part, by a National Defense Science and Engineering Graduate (NDSEG) Fellowship.

## References

P.T. Grogan (2014). *Interoperable Simulation Gaming for Strategic Infrastructure Systems Design*, Ph.D. dissertation, Massachusetts Institute of Technology, Cambridge, MA. link: [dspace.mit.edu/handle/1721.1/90169](https://dspace.mit.edu/handle/1721.1/90169)

P.T. Grogan and O.L. de Weck (2016). "Collaborative design in the Sustainable Infrastructure Planning Game," in *2016 Spring Simulation Multi-conference*, April 3-6, Pasadena, CA. 
