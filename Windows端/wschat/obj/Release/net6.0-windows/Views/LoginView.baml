   M S B A M L   `   `   `  ���� A  =wschat, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null5clr-namespace:wschat.Extensionswschat.Extensions      wschat.Views.LoginView   W SPresentationCore, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35e aSystem.Windows.Controls.Ribbon, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089R NWindowsBase, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     R NSystem.Xaml, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     @d2http://schemas.microsoft.com/expression/blend/2008     _ [Microsoft.Xaml.Behaviors, Version=1.1.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a3i+http://schemas.microsoft.com/xaml/behaviors  Dmc;http://schemas.openxmlformats.org/markup-compatibility/2006  b ^MaterialDesignThemes.Wpf, Version=4.1.0.2556, Culture=neutral, PublicKeyToken=df2a72020bd7962a:md1http://materialdesigninxaml.net/winfx/xaml/themes  *passclr-namespace:wschat.Extensions  [ WPrism.DryIoc.Wpf, Version=8.1.97.5141, Culture=neutral, PublicKeyToken=40ee6c3a2184dc59T	 PPrism.Wpf, Version=8.1.97.5141, Culture=neutral, PublicKeyToken=40ee6c3a2184dc59&prismhttp://prismlibrary.com/  	 $	��650��$	��400��  � 	Resources     CustomCheckBoxStyle �� 
TargetType �� Property �� Value �� 
TargetType �� Bullet A�Name �� Triggers P� Property	 P� Value
 �� 
TargetName��%'  &	        �� " ��.���  � �� �� " ��.R���  � $��12��$��12��$��0q�.+�A� $ BorderEllipse��
�������$��1��
������ A� $ CheckMarkEllipse��$��6��$��6��
����  �$��Center=�$��Center=�$y�Hidden=�._��� $
��Left=�$
��True��$��20,0,0,0q� P�  �@d�	IsChecked$
	 TrueY�. ��� $
 CheckMarkEllipse�� � y�$ Visible=�P�  �@d�	IsChecked$	 FalseY�. ��� $
 CheckMarkEllipse�� � y�$ Hidden=�P�  �@J�IsMouseOver$
	 True��. ��� $
 BorderEllipse�� � ��
 ��  �# 	 Prism.Services.Dialogs.Dialog   WindowStyle �� " �.���  � ��$	 600����  � ��$	 350����  �@�SizeToContent$ WidthAndHeight=���  �@�
ResizeMode$ NoResize=���  �@ WindowStartupLocation$ CenterScreen=�.��� 
������� � ColumnDefinitions �� $
��1.5* ��� $
��1.2* �.+�'  !MaterialDesignThemes.Wpf.Snackbar  -     Name$ LoginSnakeBar��$��2�� K� ZIndex$ 1��   MessageQueue 4  .MaterialDesignThemes.Wpf.MessageQueueExtension  �� $��20q�$��/Images/login.png��7  1MaterialDesignThemes.Wpf.Transitions.Transitioner  $��1�� �� SelectedIndex �� *SelectIndex+ �� FallbackValue$ 0��.@�<  6MaterialDesignThemes.Wpf.Transitions.TransitionerSlide  .��]� $��15q�$��Center=�.+��� $
��0,10q�$	��Top=�$��22(�$
��Bold"�$��欢迎使用���� $
��0,10q�)  #MaterialDesignThemes.Wpf.HintAssist   Hint$ 请输入账号��$	��Top=����� *
UserName+G� $
��0,10q�$ 请输入密码��$	��Top=�*   $wschat.Extensions.PassWordExtensions   PassWord �� *
Password+ �� Mode$ TwoWay=� �� UpdateSourceTrigger$ PropertyChanged=�*  $Microsoft.Xaml.Behaviors.Interaction   	Behaviors (	   "wschat.Extensions.PasswordBehavior	  �� $	��Top=�#��[  $��5q� d� 	IsChecked �� *
RememberMe+$ TwoWay=�.��记住密码�� $��Login��$��登录系统��$	��Top=����� *ExecuteCommand+]� $	��0,5q�$��False��.+��� $��注册账号��   Triggers +
  %Microsoft.Xaml.Behaviors.EventTrigger
   
  	EventName$ MouseLeftButtonDown��*  $Microsoft.Xaml.Behaviors.TriggerBase   Actions. 2  ,Microsoft.Xaml.Behaviors.InvokeCommandAction     CommandParameter$ ResgiterPage��   Command �� *ExecuteCommand+�� $��Right=�$��忘记密码?��  .��]� $��15q�$��Center=�.+��� $
��0,10q�$	��Top=�$��22(�$
��Bold"�$��注册账号���� $	��0,5q�$ 请输入昵称��$	��Top=����� *UserDto.NickName+�� $	��0,5q�$ 请输入用户名��$	��Top=����� *UserDto.UserName+G� $	��0,5q�$ 请输入密码��$	��Top=� �� *UserDto.Password+$ TwoWay=�$ PropertyChanged=� 	  G� $	��0,5q�$ 请再次输入密码��$	��Top=� �� *UserDto.RePassword+$ TwoWay=�$ PropertyChanged=� 	  �� $��Resgiter��$��注册账号��$	��Top=����� *ExecuteCommand+�� $
��0,10q�$��ResgiterPageByMailPage��$��邮箱注册��$	��Top=����� *ExecuteCommand+�� $	��0,0q�$��Return��$��返回登录��$	��Top=�   MaterialDesignOutlinedButton#��[ ���� *ExecuteCommand+  .��]� $��15q�$��Center=�.+��� $
��0,10q�$	��Top=�$��22(�$
��Bold"�$��注册账号���� $	��0,5q�$ 请输入昵称��$	��Top=����� *UserDto.NickName+�� $	��0,5q�$ 请输入用户名��$	��Top=����� *UserDto.UserName+G� $	��0,5q�$ 请输入密码��$	��Top=� �� *UserDto.Password+$ TwoWay=�$ PropertyChanged=� 	  G� $	��0,5q�$ 请再次输入密码��$	��Top=� �� *UserDto.RePassword+$ TwoWay=�$ PropertyChanged=� 	  �� -    ��Name$ EmailNumberTextBox��$ 请输入邮箱��$	��0,5q�$	��Top=����� *UserDto.Email+]� $	��Top=�.+��� $
��Left=�$	��0,5q�$ 邮箱验证码��$	��160�� � Cursor$ IBeam������ *UserDto.Code+�� -    ��Name$ SendCodeButton��$��0,0,0,0q�$��Right=�$��SendCode��$��发送验证码��$��13(�$��90��$��0q����	#FF0078D7���� *ExecuteCommand+����   �� Color$
  Gray��! �� Opacity$
! 0.25Q��� $	��0,5q�$��ResgiterByMail��$��注册账号��$	��Top=����� *ExecuteCommand+�� $	��0,0q�$��Return��$��返回登录��$	��Top=�#��[ ���� *ExecuteCommand+