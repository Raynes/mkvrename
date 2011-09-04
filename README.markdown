# mkvrename

MakeMKV is an excellent application for ripping television show episodes from DVDs. It rips them episode-by-episode and saves them to mkv files. These files are named in the order they appear on the disc. title00.mkv, title01.mkv, and so on. This is not a suitable format for metadata scrapers like those in Plex and XBMC. Personally, I like to name my shows like so: <show title> - S<season>E<episode>.mkv.

This application serves a very specific purpose. It renames mkv files of the format titlexx.mkv to the format above. The episode number is determined based on the highest numbered episode in the directory. If no episodes are present, the application asks for a title and season number and will name the files appropriately.
