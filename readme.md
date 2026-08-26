### 

### Git

(repository link: https://github.com/Desublimation/Computer-Science-Master.git)



==================================

|regular add new in git repository|

===================================

(1) git add . 

(2) git commit -m "{update notes}"

(3) git push -u origin main / git push (for quick)



=====================

|git sparse-checkout|

=====================

**\*NOTE**: this is a Git command used to reduce your local working directory to only a specific subset of tracked files or folders. 



(1) Start invoke sparse checkout

&#x20;   *git sparse-checkout init --cone*

(2) assign the folder you want to **remain**

&#x20;   *git sparse-checkout set {folder's name} / git sparse-checkout set {} {}*(for >= 2 folder)



(3) to recover the Repository so you can see all folder in your local machine

&#x20;   *git sparse-checkout disable*



**NOTICE:** After done the sparse checkout, you can update new files with normal procedure refer to "regular add new in git repository" 

