# Quick Start #

Create a text field and attach the QuickSearchPopup to it:
```
JTextField quickSearchField = new JTextField(40);
QuickSearchPopup quickSearchPopup = new QuickSearchPopup(quickSearchField, this);
quickSearchPopup.setSearchRows(rowsToSearch);
```


See the full source code: [SimpleExample.java](http://code.google.com/p/java-quick-search-popup/source/browse/src/example/java/se/lesc/quicksearchpopup/example/SimpleExample.java)