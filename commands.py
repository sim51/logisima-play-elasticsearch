# -*- coding: utf-8 -*-

###
 #  This file is part of LogiSima.
 #
 #  LogiSima is free software: you can redistribute it and/or modify
 #  it under the terms of the GNU General Public License as published by
 #  the Free Software Foundation, either version 3 of the License, or
 #  (at your option) any later version.
 #
 #  LogiSima is distributed in the hope that it will be useful,
 #  but WITHOUT ANY WARRANTY; without even the implied warranty of
 #  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 #  GNU General Public License for more details.
 #
 #  You should have received a copy of the GNU General Public License
 #  along with LogiSima.  If not, see <http://www.gnu.org/licenses/>.
###

# Here you can create play commands that are specific to the module, and extend existing commands
import os, os.path
import getopt
import sys
import subprocess

MODULE = 'esearch'

# Commands that are specific to your module

COMMANDS = ['es:help','es:generate']

def execute(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")

    if command == "es:help":
        print "~ Help for logisima-elasticsearch module"
        print "~ Available commands are:"
        print "~ ~~~~~~~~~~~~~~~~~~~~~~~"
        print "~ generate       Generate the elasticsearch schema from your model (to file conf/esearch.conf)"
        print "~     with --filename you can specify filename (without the extension !)"
        print "~     with --output you can specify the directory where yaml file will be written (conf by default)"
        print       
        sys.exit(0)
    
    if command == "es:generate":
        print "~ Generating schema"
        print "~ "
        java_cmd = app.java_cmd([], None, "play.modules.elasticsearch.mapping.ESearchMapping", args)
        try:
            subprocess.call(java_cmd, env=os.environ)
        except OSError:
            print "Could not execute the java executable, please make sure the JAVA_HOME environment variable is set properly (the java executable should reside at JAVA_HOME/bin/java). "
            sys.exit(-1)
        print
        
# This will be executed before any command (new, run...)
def before(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")