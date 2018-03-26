

import sys
from pytube import YouTube

arg1 = sys.argv[1]
loc = sys.argv[2]

print(arg1 + '\n')

yt = YouTube(arg1)
streams = yt.streams.filter(only_audio=True).filter(subtype='mp4')
streams.first().download(loc)
print("something beautiful" + '\n')

