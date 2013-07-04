Tankz
-----

This is a port to libgdx of Gamadu's Slick2D-based Tankz demo found here: https://code.google.com/p/gamadu-tankz/source/checkout

The demo is incomplete, and the port differs in several ways:

* replaced Slick2D with libgdx
* added enemy AI system
* removed shooting
* removed tank turret (tower) and gun barrel
* removed tank treads

This functionality could be added back in and serve as a starting point for a top-down game using Box2D / Artemis / libgdx.

This project served primarily for me to learn the Artemis Entity System.  Hopefully it will act as a springboard for others!

Basic operation
---------------

* W/S - to accelerate forward or backward
* A/D - to steer left and right
* mouse scroll button to zoom in / out

Note: Assumes keyboard support on Android device.

Related software
----------------

* Eclipse IDE
* Android SDK (for Android deployment)
* libgdx (http://github.com/libgdx/libgdx)
* Artemis Entity System (http://github.com/tescott/artemis)

General Notes
-------------

The versions of libdx and Artemis included in this repo are several months out of date.

HTML5 project is included but has not been updated, tested or verified. 

Pull requests will be welcome!

