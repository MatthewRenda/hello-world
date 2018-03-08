Design Report
=============

Commands
---------
**Log**
  (log containing all information created by the user)
  
**writeEntry**
    WriteEntry is called whenever any output to the log is needed 
    used by: start(), stop(), description()
    
**Rename**
  Re-names the task specified throughout the entire log file. Any instance of the task will be replaced with the new name specified.
  
**Delete**
  Deletes the task specified thoughout the entire log file. Any instance of the task specified will be deleted.

**Time**
  Time collects the beginning start time and the last known stop time of the task specified, returning both.

**Start / Stop**
I decided to combine the two commands into one function called Stamp. I did this with the mindset that the functionality of the two 
options are identical, therefore I can easily call the same function twice rather than building two different functions. The function consists of date formatting and then calling a date function to obtain an accurate local time to store into our format. If there is no file created, calling start() will create the file.

**description**
The describe function formats a "line" to be passed through the function and later, either stay as the formatted line based on if there were previous descriptions called using the same command and data values, or if this is it's second instance it will turn into "addLine" and add onto the previous description line. I use a scanner to scan through my data in my local file"Task_Manager_Log.txt" to search any instances of describe being called. The call will only add on to the first instance of the original call which is a problem. In the future I will design it so that the user can add as many extra additions to any description previously made. I chose to write everything to a file and instead of using any linked list maps or graph I decided to read through my file and take any desired course of action as I went.  

**Summary**
My Summary function is built using a Scanner to read through my data file. The function is set up so that if there is a known args[1] then it shoots to printing out the specified data in the file based on the task input by the user. Then second option of my Summary function is when the program reads that there is no args[1] and prints out the entire file.

**Output**
For this project I chose to output my data to a "Task_Manager_Log.txt" file.





