#include <iostream>
#include <string>
#include <fstream>
#include <algorithm>
#include <unordered_map>
#include <vector>
#include <sstream>
using namespace std;

int main()
{
    ifstream fin;
    fin.open("outputacm2.txt");

    ofstream myfile;
    myfile.open("data2.csv");

    bool inCites = 0;
    bool first = 1;
    vector<string>myvector;
    for(string line; getline(fin, line); )
    {
        if(line[1]=='%')
        {
            line.erase(0,2);
            myvector.push_back(line);
            inCites = 1;
        }
        if(line[1] == '@' && inCites == 1)
        {
            for(int i = 0; i <myvector.size();i++)
            {
                cout << myvector[i]<< ",";
            }
            cout << endl;
            inCites = 0;
            myvector.clear();
        }
        else if(line[1] == '@' && inCites == 0)
        {
            if(first == 1)
                first = 0;
            else
                cout << "-1," <<endl;
        }
 


    }

        if(inCites == 0)
            cout << "-1," <<endl;
        else
        {
            for(int i = 0; i <myvector.size();i++)
            {
                cout << myvector[i]<< ",";
            }
        }  


}


/*int main()
{
    unordered_map<string,int> myMap;
    
    ifstream fin;
    fin.open("outputacm2.txt");
    //ofstream myfile;
    //myfile.open("data2.csv");
    int n = 0;
    int flag = 0;
    vector<string>myvector;
    for(string line; getline(fin, line); )
    {
        if(line[1]=='%')
        {
            line.erase(0,2);
            myvector.push_back(line);
        }
        if(line[1]=='*')
        { 
            myvector.push_back("-1");
        }
        else if(line[1]=='%')
        {
            
            line.erase(0,2);
            myvector.push_back(line);
        }
        for(int i = 0; i <myvector.size();i++)
            {
                cout << myvector[i]<< ",";
               
            }
            cout << endl;
            myvector.clear();

    }
    //for (unordered_map<string,int>::iterator it=myMap.begin(); it!=myMap.end(); ++it)
        //cout << it->first << " => " << it->second << '\n';
    fin.close();
    

}
*/







