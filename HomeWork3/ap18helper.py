
import logging
import sys
import pathlib
import re
import urllib.request
import csv
import subprocess

def rebuild(file, package_name) -> None:
    """ 
    Given a file object, it finds rebuilds it according to
    the homework specifications according to the provided package name

   Keyword arguments:
    file -- the file object
    package_name -- the package name in the java file
    """
    
    file_path = pathlib.Path(file)
    # I get all the directory names
    file_absolute_path = file_path.parent.parts
    # In the last position of this tuple is the directory
    # of the file (parent was not ok to use because it uses
    # the full path)
    if package_name == file_absolute_path[len(file_absolute_path) - 1]:
        logging.info('This file is already in the correct folder')
    else:
        # I have to check if any subdirectory exists for package pname
        # I create the path obj concatenating the folder and 
        # the pattern
        target_folder = file_path.parent / package_name
        if not target_folder.exists():
            # create dir
            logging.info('Created directory ' + str(target_folder))
            target_folder.mkdir()

        logging.info('Moving file to ' + str(target_folder))
        # This works as a "move file"
        file_path.rename(target_folder / file_path.name)

def locate_testing_directory(file_path) -> pathlib.Path:
    """ 
    Given a file_path, it finds its testing directory and returns it

   Keyword arguments:
    file_path -- the path of the file 
    """
    
    if file_path.suffix == '.py' or file_path.suffix == '.hs':
        testing_directory = file_path.parent
    else: # file suffix is .java
        package_name = parse_package(file_path)
        if package_name:
            # Then the testing directory is the parent
            testing_directory = file_path.parents[1] 
        else:
            # Then the testing directory is the current
            # parent here takes the current directory 
            testing_directory = file_path.parent
    return testing_directory

def issue_commands(cmd_list, testing_directory) -> None:
    """ 
    Given a command list, it issues them in the testing directory 

   Keyword arguments:
    cmd_list -- cmd_list the string in the AP_TestRegistry
    testing_directory -- the directory in which commands have to be issued
    """
    commands = cmd_list.split(';')
    for cmd in commands:
        cmd = cmd.strip()
        logging.info('Issuing commands \'' + cmd + '\'')
        # The directory in which the command is issued is the
        # one specified in cwd arg
        subprocess.call(cmd, shell=True, cwd=str(testing_directory))

def parse_package(file):
    """ 
    Given a java file object, it parses it and retrieves the package pname
    Returns None if the file had no package definition, or the package name

   Keyword arguments:
    filename -- the java file object
    """

    # This regular expression will match only the package statement in
    # a Java file, and will have the package definition in group 1
    reg_expression = re.compile(r'package (\w*);')
    # pattern will be the package name or None if it was not in it
    pattern = None
    with open(str(file)) as f:
        try:
            for line in f:
                # I just search for any matching pattern
                line = line.strip()
                match = reg_expression.search(line)
                if match:
                    # found a match, the package name is in grp 1
                    pattern = match.group(1)
                    # I can stop with this file, I'm not gonna find
                    # any more package statement
                    break
        except:
            logging.info('Error with file ' + str(file))
            return None

    return pattern

def end_program():
    """ Ends the main loop of the program."""

    print('Thank you, bye!')
    exit(0)

def raj2jar_caller():
    """ Asks the user which path to call raj2jar on, and calls it"""

    path = input('Please, insert the absolute path of the root'
        ' directory to find: ')
    raj2jar(path)

def raj2jar(root):
    """ 
    Transforms every file inside the root directory with a .raj extension
    to .jar, also following every root subdirectory

   Keyword arguments:
    root -- the root folder to start 
    """

    p = pathlib.Path(root)
    if not p.exists() or not p.is_dir:
        print('Sorry, couldn\'t find your root directory')
        return
    # I have the root directory, I have to find every raj files

    # I create a list with all files in the subdirectory, by also
    # checking the nested directories
    raj_files = list(p.glob('**/*.raj'))
    for file in raj_files:
        logging.info("changing to \'.jar\' the file: " + str(file))
        # splitting on the dot, and taking all the prefix
        try:
            file.rename(file.with_suffix('.jar'))
        except:
            logging.info('There was already a file named ' 
                + str(file.with_suffix('.jar')))

def collect_sources_caller():
    """ Asks the user which parameters to pass to call collect_sources, 
    and calls it"""

    path = input('Please, insert the absolute path of the root'
        ' directory to find: ')
    filename = input('Please, insert the name of the file to create: ')
    collect_sources(path, filename)

def collect_sources(root, sources):
    """ 
    Creates (or modifies) a file named sources in the local working 
    directory and writes there every Python, Java and Haskell source
    file name

   Keyword arguments:
    root -- the root folder to start 
    sources -- the name of the file to create
    """
    # Path is an object of a class imported by pathlib which handles
    # in an oop fashion the file system
    p = pathlib.Path(root)
    if not p.exists() or not p.is_dir:
        # root has to be a directory
        print('Sorry, couldn\'t find your root directory')
        return

    # I have to find all the files with those extensions. The double '*'
    # is used because I have to navigate subdirectories too, and this
    # is done by path.glob only if presented with a double asterisc pattern
    sources_extensions = ('**/*.py', '**/*.hs', '**/*.java')
    sources_files = []
    for ext in sources_extensions:
        sources_files.extend(p.glob(ext))

    # opening/creating the file to write 
    with open(root + '/' + sources, 'w') as file:
        logging.info("writing list of files to file: " + sources)
        for filename in sources_files:
            # relative to takes the relative path starting from
            # the root folder provided by the user
            file.write(str(filename.relative_to(p)) + '\n')

def rebuild_packages_caller():
    """ Asks the user which path to call rebuild_packages on, and calls it"""

    path = input('Please, insert the absolute path of the root'
        ' directory to rebuild: ')
    rebuild_packages(path)

def rebuild_packages(root):
    """
    This method changes the package statement of every java file inside
    the root directory provided (and every subdirectory nested) according
    to the rules provided in the homework text

    Keyword arguments:
        root -- the root folder to start 
    """
    p = pathlib.Path(root)
    if not p.exists() or not p.is_dir:
        print('Sorry, couldn\'t find your root directory')
        return

    # Finding every java file in the whole subtree of root
    java_files = list(p.glob('**/*.java'))
    for file in java_files:
        logging.info("Checking \'" + str(file) + '\'')
        # pattern will be the package name or None if it was not in it
        
        package_name = parse_package(file)
        if package_name:
            # found a package, the package name is in var package_name
            # in var file I have the file I am working on
            rebuild(file, package_name)
 
def download_tests_caller():
    """ Asks the user which path and URL to call 
    download_tests on, and calls it"""

    path = input('Please, insert the absolute path of the root'
        ' directory: ')
    url = input('Please, insert the URL where the test files are '
        'located, or just press enter if you want to use the default'
        ' one: ')
    if url == '':
        download_tests(path)
    else:
        download_tests(path, url)

def download_tests(root, url = 'http://pages.di.unipi.it/corradini/'
    'Didattica/AP-18/PROG-ASS/03/Test'):

    """
    This method download every test file from given url and places
    every file specified by AP_registry.csv in its testing directory

    Keyword arguments:
        root -- the root folder to start 
        url -- the URL where the tests files have to be downloaded
               (default is the above URL)
    """
    p = pathlib.Path(root)

    if not p.exists() or not p.is_dir:
        print('Sorry, couldn\'t find your root directory')
        return

    # First thing to do is download the registry
    logging.info('Downloading AP_testRegistry.csv latest version')
    try:
        response = urllib.request.urlopen(url + '/AP_TestRegistry.csv')
    except:
        logging.info('Sorry, couldn\'t create a connection')
        return
    # If i get there then I had a successful connection and have
    # the latest version of the registry

    data = response.read()
    text = data.decode('utf-8')
    text = text.split('\n')
    # I use the csv module to split and create a dictionary of records
    reader = csv.DictReader(text, delimiter = ',', skipinitialspace = True)
    for line in reader:
        # I retrieve the 3 parts of the dictionary
        filename = line['filename']
        # test_files is a list of strings (representing the files)
        test_files = line['testfiles'].split(':')
        command = line['command']

        # I try and find the file filename
        file_list = list(p.glob('**/*' + filename))
        if len(file_list) == 0:
            # I didn't find filename, then I need to go to next line in the
            # file AP_TestRegistry
            continue
        else:
            file_path = pathlib.Path(file_list.pop())
            # I found the file I was searching for (filename in the CSV file)
            
        # I have to find the testing directory for the file found before
        testing_directory = locate_testing_directory(file_path)

        # I get in this for loop only if I found the file filename
        for f in test_files:
            # removing spaces and tabs
            f = f.strip()
            if len(f) == 0:
                continue
            
            logging.info('Downloading latest version of ' + f)
            # I try and download the requested files
            try:
                # New file path is the file path of the new file in 
                # the testing directory computed before
                new_file_path = testing_directory / f
                # I use urllib to download the file and store it
                response = urllib.request.urlretrieve(url + '/' + f, 
                    filename = str(new_file_path))
                logging.info('The new file path will be '
                    + str(new_file_path))
            except:
                logging.info('Sorry, couldn\'t create a connection '
                    'for ' + str(f))
                continue       

        # I have to issue the command now
        issue_commands(command, testing_directory)

def main() -> None:

    logging.basicConfig(level=logging.INFO)

    # switcher is a function dictionary that I use to switch between 
    # the 4 functions I have to provide to the user
    switcher = {
        1: raj2jar_caller,
        2: collect_sources_caller,
        3: rebuild_packages_caller,
        4: download_tests_caller,
        5: end_program
    }

    while True:
        print('Please select the next operation to be executed '
            'by typing the relative number:')
        print('\t1_ Renaming archive (from .raj to .jar)')
        print('\t2_ Producing the list of files to be printed')
        print('\t3_ Rebuilding the structure of packages')
        print('\t4_ Retrieving the files for testing')
        print('\t5_ Terminate this process')
        opt = input("Insert the number now: ")
        try:
            # I retrieve the caller function from the function dictionary
            # switcher to call, in case I don't find the right one I 
            # obtain a default function that tells the user that the 
            # number passed was not valid
            func = switcher.get(int(opt), lambda: print("Invalid argument"))
        except:
            # parse error during int(opt), so not an int was passed
            print("Error: not a number, please insert a valid value")
            input("Press enter to continue: ")
            continue
        
        #I then invoke the function that was requested by the user
        # actually I call the caller function for given task
        func()
        # I ask for the user permission before proceeding to next iteration
        input("Press enter to acknowledge the function execution: ")

if __name__ == "__main__" :
    main()


