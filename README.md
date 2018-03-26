# Ze Sloka control/automation server

Application jumble together necessary functionality to remotely control and automate Ze Sloka radio station. 
Each functionality exists as a separate entity (Command).  If necessary, communication between commands happens through application internal or remote dispatch service. In normal conditions, Commands should never try to communicate directly.

The existing list of commands can be accessed like this:
